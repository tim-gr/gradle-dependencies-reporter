package io.github.tim_gr.dependencies_reporter.dependents

internal data class ProjectNode(
    val name: String,
    val children: List<ProjectNode> = emptyList(),
    val isCycleTerminal: Boolean = false
)
