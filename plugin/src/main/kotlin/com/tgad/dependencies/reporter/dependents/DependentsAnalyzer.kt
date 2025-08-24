package com.tgad.dependencies.reporter.dependents

internal class DependentsAnalyzer {

    fun buildDependentsTree(target: String, dependencyMap: Map<String, List<String>>): ProjectNode {
        val inverseGraph = buildInverseGraph(dependencyMap)
        val visitedPath = ArrayDeque<String>()
        return dfs(target, inverseGraph, visitedPath) { cycle ->
            println("[Warning] Cycle detected: ${cycle.joinToString(" -> ") { it }}")
        }
    }

    private fun buildInverseGraph(dependencyMap: Map<String, List<String>>): Map<String, List<String>> {
        val map = mutableMapOf<String, MutableList<String>>()
        dependencyMap.forEach { key, value ->
            value.forEach { dep ->
                map.getOrPut(dep) { mutableListOf() }.add(key)
            }
            // Ensure every project appears in the map, even if it has no dependents
            map.getOrPut(key) { mutableListOf() }
        }
        return map.mapValues { (_, v) -> v.distinct().sorted() }
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
        val dependents = inverseGraph[current]!!

        val children = dependents.map { dep ->
            dfs(dep, inverseGraph, path, onCycle)
        }

        path.removeLast()
        return ProjectNode(current, children)
    }
}
