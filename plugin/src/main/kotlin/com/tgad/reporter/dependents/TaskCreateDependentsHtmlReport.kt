package com.tgad.reporter.dependents

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

internal abstract class TaskCreateDependentsHtmlReport : DefaultTask() {

    @get:Input
    abstract val inputStartModuleName: Property<String>

    @get:Input
    abstract val inputModuleDependencies: MapProperty<String, List<String>>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        if (inputStartModuleName.get().isBlank()) {
            throw IllegalArgumentException("Please provide the 'module' parameter, e.g., -Pmodule=:A")
        }

        if (!inputModuleDependencies.get().keys.contains("${inputStartModuleName.get()}")) {
            throw IllegalArgumentException("Module '${inputStartModuleName.get()}' not found.")
        }

        val rootNode = DependentsAnalyzer().buildDependentsTree(
            target = inputStartModuleName.get(),
            dependencyMap = inputModuleDependencies.get(),
        )

        val htmlText = HtmlRenderer().render(
            rootNode = rootNode,
            nameStartNode = inputStartModuleName.get(),
        )

        val outputFile = outputDir.file("dependents-report.html").get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(htmlText, Charsets.UTF_8)
    }
}
