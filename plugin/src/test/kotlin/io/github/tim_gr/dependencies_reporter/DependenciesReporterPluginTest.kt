package io.github.tim_gr.dependencies_reporter

import io.github.tim_gr.dependencies_reporter.dependents.TaskDependentsHtmlReport
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GradleDependenciesReporterPluginTest {

    @Test
    fun `plugin registers dependentsHtmlReport task on project`() {
        val rootProject = ProjectBuilder.builder().withName("root").build()

        rootProject.plugins.apply(PLUGIN_NAME)

        assertNotNull(rootProject.tasks.findByName(TASK_DEPENDENTS_HTML_REPORT))
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
        val rootProject = ProjectBuilder.builder().withName("root").build()
        ProjectBuilder.builder().withName("sub")
            .withParent(rootProject).build()

        rootProject.plugins.apply(PLUGIN_NAME)

        val task = rootProject.tasks.findByName(TASK_DEPENDENTS_HTML_REPORT)
                as TaskDependentsHtmlReport

        assertNotNull(task.outputDir.orNull)

        val dependencies = task.inputModuleDependencies.get()
        assertEquals(true, dependencies.containsKey(":sub"))
    }

    companion object {
        private const val PLUGIN_NAME = "io.github.tim-gr.dependencies-reporter"
        private const val TASK_DEPENDENTS_HTML_REPORT = "dependentsHtmlReport"
    }
}
