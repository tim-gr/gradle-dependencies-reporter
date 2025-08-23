package com.tgad.reporter

data class ProjectNode(
    val name: String,
    val children: List<ProjectNode> = emptyList(),
    val isCycleTerminal: Boolean = false
)
