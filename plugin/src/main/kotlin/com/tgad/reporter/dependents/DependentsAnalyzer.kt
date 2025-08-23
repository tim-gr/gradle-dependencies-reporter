package com.tgad.reporter.dependents

import com.tgad.reporter.ProjectNode

class DependentsAnalyzer {

    fun buildDependentsTree(target: String, dependencyMap: Map<String, List<String>>): ProjectNode {
        val inverseGraph = buildInverseGraph(dependencyMap)
        val visitedPath = ArrayDeque<String>()
        return dfs(target, inverseGraph, visitedPath) { cycle ->
            println("[Warning] Cycle detected: ${cycle.joinToString(" -> ") { it }}")
        }
    }

    private fun buildInverseGraph(dependencyMap: Map<String, List<String>>): Map<String, List<String>> {
        val map = mutableMapOf<String, MutableList<String>>()

        dependencyMap.keys.forEach { map[it] = mutableListOf() }

        dependencyMap.forEach { key, value ->
            value.forEach {
                map.getValue(it).add(key)
            }
        }

        return map.mapValues { (_, v) -> v.distinct().sortedBy { it } }
    }

    private fun dfs(
        current: String,
        inverseGraph: Map<String, List<String>>,
        path: ArrayDeque<String>,
        onCycle: (List<String>) -> Unit
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
