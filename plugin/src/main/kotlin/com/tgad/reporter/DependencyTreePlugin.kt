package com.tgad.reporter

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyTreePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateHtmlReport", GenerateDependentsTreeTask::class.java)
    }
}
