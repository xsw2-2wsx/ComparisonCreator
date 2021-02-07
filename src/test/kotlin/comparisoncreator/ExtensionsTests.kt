package comparisoncreator

import comparisoncreator.util.allPossibleSubcollectionsSized
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

@Test
fun testAllPossibleSubcollectionsSizedExtension() {
    val testCollection = listOf(1, 2, 3)

    val result = testCollection.allPossibleSubcollectionsSized(2);

    assertEquals(3, result.size)

    result.forEach {
        assertEquals(it.size, 2)
    }

    assertDoesNotThrow {
        result.first { it.containsAll(listOf(1, 2)) }
        result.first { it.containsAll(listOf(1, 3)) }
        result.first { it.containsAll(listOf(2, 3)) }
    }
}