package com.tgad.dependencies.reporter

import com.tgad.dependencies.reporter.dependents.TaskDependentsHtmlReport
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

internal class DependenciesReporterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register(
            "dependentsHtmlReport",
            TaskDependentsHtmlReport::class.java
        ) {
            if (project != project.rootProject) {
                throw GradleException("This plugin must be applied to the root project.")
            }

            val moduleParam = project.providers.gradleProperty("module")
                .orElse("")
            inputStartModuleName.set(moduleParam)

            outputDir.set(project.layout.buildDirectory.dir("reports"))

            val moduleDependencies = project.allprojects.associate { proj ->
                val dependencies = proj.configurations.flatMap { configuration ->
                    configuration.dependencies.withType(ProjectDependency::class.java)
                        .map { it.path }
                        .filter { it != proj.path }
                }.distinct()
                proj.path to dependencies
            }
            inputModuleDependencies.set(moduleDependencies)
        }
    }
}
