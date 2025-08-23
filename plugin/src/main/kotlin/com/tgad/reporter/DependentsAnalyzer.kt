package com.tgad.reporter

import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.artifacts.ProjectDependency

class DependentsAnalyzer {

    /**
     * Builds the dependents tree: which projects (directly or transitively) depend on [target].
     */
    fun buildDependentsTree(target: Project, logger: Logger): ProjectNode {
        val inverseGraph = buildInverseGraph(target.rootProject)
        val visitedPath = ArrayDeque<Project>()
        return dfs(target, inverseGraph, visitedPath) { cycle ->
            logger.warn("[dep-tree] Cycle detected: ${cycle.joinToString(" -> ") { it.path }}")
        }
    }

    /**
     * Builds a map: key = project, value = list of projects that depend on it.
     */
    private fun buildInverseGraph(root: Project): Map<Project, List<Project>> {
        val map = mutableMapOf<Project, MutableList<Project>>()

        // Initialize keys
        root.allprojects.forEach { map[it] = mutableListOf() }

        root.allprojects.forEach { proj ->
            proj.configurations.forEach { cfg ->
                cfg.dependencies.withType(ProjectDependency::class.java).forEach { dep ->
                    val target = dep.dependencyProject
                    map.getValue(target).add(proj)
                }
            }
        }

        // De-duplicate & sort dependents for stable output
        return map.mapValues { (_, v) -> v.distinct().sortedBy { it.path } }
    }

    private fun dfs(
        current: Project,
        inverseGraph: Map<Project, List<Project>>,
        path: ArrayDeque<Project>,
        onCycle: (List<Project>) -> Unit
    ): ProjectNode {
        if (path.contains(current)) {
            onCycle(path.toList() + current)
            return ProjectNode(current, isCycleTerminal = true)
        }

        path.addLast(current)
        val dependents = inverseGraph[current].orEmpty()

        val children = dependents.map { dep ->
            dfs(dep, inverseGraph, path, onCycle)
        }

        path.removeLast()
        return ProjectNode(current, children)
    }
}
