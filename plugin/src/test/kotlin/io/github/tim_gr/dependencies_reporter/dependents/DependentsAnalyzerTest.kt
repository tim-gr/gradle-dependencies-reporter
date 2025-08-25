package io.github.tim_gr.dependencies_reporter.dependents

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DependentsAnalyzerTest {

    private val analyzer = DependentsAnalyzer()

    @Test
    fun `single dependency chain A - B - C`() {
        val dependencyMap = mapOf(
            ":A" to listOf(":B"),
            ":B" to listOf(":C"),
            ":C" to emptyList()
        )

        val tree = analyzer.buildDependentsTree(":C", dependencyMap, emptyList())

        assertEquals(":C", tree.name)
        assertEquals(1, tree.children.size)
        assertEquals(":B", tree.children[0].name)
        assertEquals(1, tree.children[0].children.size)
        assertEquals(":A", tree.children[0].children[0].name)
    }

    @Test
    fun `multiple dependents`() {
        val dependencyMap = mapOf(
            ":A" to listOf(":C"),
            ":B" to listOf(":C"),
            ":C" to emptyList()
        )

        val tree = analyzer.buildDependentsTree(":C", dependencyMap, emptyList())

        assertEquals(":C", tree.name)
        assertEquals(2, tree.children.size)
        assertNotNull(tree.children.find { it.name == ":A" })
        assertNotNull(tree.children.find { it.name == ":B" })
        assertNull(tree.children.find { it.name == ":C" })
    }

    @Test
    fun `no dependents`() {
        val dependencyMap = mapOf(
            ":A" to listOf(":B"),
            ":B" to emptyList()
        )

        val tree = analyzer.buildDependentsTree(":A", dependencyMap, emptyList())

        assertEquals(":A", tree.name)
        assertEquals(0, tree.children.size)
    }

    @Test
    fun `cycle detection A - B - A`() {
        val dependencyMap = mapOf(
            ":A" to listOf(":B"),
            ":B" to listOf(":C"),
            ":C" to listOf(":A")
        )

        val tree = analyzer.buildDependentsTree(":A", dependencyMap, emptyList())

        val cNode = tree.children[0]
        val bNode = cNode.children[0]
        val aNode = bNode.children[0]
        assertFalse(cNode.isCycleTerminal)
        assertFalse(bNode.isCycleTerminal)
        assertTrue(aNode.isCycleTerminal)
        assertEquals(":A", aNode.name)
        assertEquals(":B", bNode.name)
        assertEquals(":C", cNode.name)
    }

    @Test
    fun `disconnected modules appear`() {
        val dependencyMap = mapOf(
            ":A" to listOf(":B"),
            ":B" to emptyList(),
            ":C" to emptyList()
        )

        val treeC = analyzer.buildDependentsTree(":C", dependencyMap, emptyList())
        assertEquals(":C", treeC.name)
        assertEquals(0, treeC.children.size)
    }

    @Test
    fun `excluded modules are not shown`() {
        val dependencyMap = mapOf(
            ":A" to listOf(":B"),
            ":B" to listOf(":C"),
            ":C" to emptyList()
        )

        val treeWithExcludedModule = analyzer.buildDependentsTree(":C", dependencyMap, listOf(":B"))
        assertEquals(":C", treeWithExcludedModule.name)
        assertEquals(0, treeWithExcludedModule.children.size)

        val treeWithoutExclusions = analyzer.buildDependentsTree(":C", dependencyMap, emptyList())
        assertEquals(":C", treeWithoutExclusions.name)
        assertEquals(1, treeWithoutExclusions.children.size)
        assertEquals(":B", treeWithoutExclusions.children[0].name)
    }
}
