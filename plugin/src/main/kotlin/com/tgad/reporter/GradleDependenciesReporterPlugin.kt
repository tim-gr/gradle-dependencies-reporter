package com.tgad.reporter

import com.tgad.reporter.dependents.TaskCreateDependentsHtmlReport
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleDependenciesReporterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register(
            "createDependentsHtmlReport",
            TaskCreateDependentsHtmlReport::class.java
        ) {
            inputStartModuleName.set(project.path)

            outputDir.set(project.layout.buildDirectory.dir("reports"))

            val moduleDependencies = project.rootProject.allprojects.associate { proj ->
                val dependencies = proj.configurations.flatMap { configuration ->
                    configuration.dependencies.withType(org.gradle.api.artifacts.ProjectDependency::class.java)
                        .map { it.path }
                }.distinct()
                proj.path to dependencies
            }
            inputModuleDependencies.set(moduleDependencies)
        }
    }
}
