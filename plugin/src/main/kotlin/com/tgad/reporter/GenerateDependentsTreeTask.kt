package com.tgad.reporter

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

abstract class GenerateDependentsTreeTask : DefaultTask() {

    @TaskAction
    fun generate() {
        val analyzer = DependentsAnalyzer()
        val renderer = HtmlRenderer()

        val root = project
        val logger = project.logger

        val tree = analyzer.buildDependentsTree(root, logger)

        val html = renderer.render(tree, "Dependents Tree for ${root.path}")

        val out = project.layout.buildDirectory.file("reports/dependents-tree/index.html").get().asFile
        out.parentFile.mkdirs()
        out.writeText(html, Charsets.UTF_8)

        logger.lifecycle("[dep-tree] Wrote ${out.absolutePath}")
    }
}
