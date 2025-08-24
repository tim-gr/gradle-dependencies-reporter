package com.tgad.reporter

import com.tgad.reporter.dependents.TaskCreateDependentsHtmlReport
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GradleDependenciesReporterPluginTest {

    @Test
    fun `plugin registers createDependentsHtmlReport task on project`() {
        val project = ProjectBuilder.builder().withName("root").build()

        project.plugins.apply(PLUGIN_NAME)

        assertNotNull(project.tasks.findByName(TASK_DEPENDENTS_HTML_REPORT))
    }

    @Test
    fun `plugin throws when applied to subproject`() {
        val rootProject = ProjectBuilder.builder().withName("root").build()
        val subProject = ProjectBuilder.builder().withName("sub")
            .withParent(rootProject).build()

        subProject.plugins.apply(PLUGIN_NAME)

        assertThrows<GradleException> {
            subProject.tasks.findByName(TASK_DEPENDENTS_HTML_REPORT)
        }
    }

    @Test
    fun `plugin configures input properties`() {
        val project = ProjectBuilder.builder().withName("root").build()
        project.plugins.apply(PLUGIN_NAME)

        val task = project.tasks.findByName(TASK_DEPENDENTS_HTML_REPORT)
                as TaskCreateDependentsHtmlReport

        // It should set an outputDir
        assertNotNull(task.outputDir.orNull)

        // Module dependencies map should include at least the root project
        val deps = task.inputModuleDependencies.get()
        assertEquals(true, deps.containsKey(":"))
    }

    companion object {
        private const val PLUGIN_NAME = "com.tgad.gradle.dependencies.reporter"
        private const val TASK_DEPENDENTS_HTML_REPORT = "dependentsHtmlReport"
    }
}
