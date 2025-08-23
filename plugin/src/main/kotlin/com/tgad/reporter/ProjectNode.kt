package com.tgad.reporter

import org.gradle.api.Project

data class ProjectNode(
    val project: Project,
    val children: List<ProjectNode> = emptyList(),
    val isCycleTerminal: Boolean = false
)
