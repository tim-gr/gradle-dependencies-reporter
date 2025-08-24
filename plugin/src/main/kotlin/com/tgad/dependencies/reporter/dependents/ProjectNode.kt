package com.tgad.dependencies.reporter.dependents

internal data class ProjectNode(
    val name: String,
    val children: List<ProjectNode> = emptyList(),
    val isCycleTerminal: Boolean = false
)
