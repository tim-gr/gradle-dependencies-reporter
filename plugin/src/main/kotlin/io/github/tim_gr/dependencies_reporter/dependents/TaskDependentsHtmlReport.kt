package io.github.tim_gr.dependencies_reporter.dependents

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

internal abstract class TaskDependentsHtmlReport : DefaultTask() {

    @get:Input
    abstract val inputStartModuleName: Property<String>

    @get:Input
    abstract val inputExcludedModules: Property<String>

    @get:Input
    abstract val inputModuleDependencies: MapProperty<String, List<String>>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val startModuleName = inputStartModuleName.get()

        if (startModuleName.isBlank()) {
            throw IllegalArgumentException("Please provide the 'module' parameter, e.g., -Pmodule=:A")
        }

        if (!inputModuleDependencies.get().keys.contains(startModuleName)) {
            throw IllegalArgumentException("Module '$startModuleName' not found.")
        }

        val excludedModules = inputExcludedModules.get()
            .split(',')
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val rootNode = DependentsAnalyzer().buildDependentsTree(
            target = startModuleName,
            dependencyMap = inputModuleDependencies.get(),
            excludedModules = excludedModules,
        )

        val htmlText = HtmlRenderer().render(
            rootNode = rootNode,
            nameStartNode = startModuleName,
            excludedModules = excludedModules,
        )

        val outputFile = outputDir.file(
            "dependents-report$startModuleName.html"
        ).get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(htmlText, Charsets.UTF_8)
    }
}
