package comparisoncreator

import comparisoncreator.data.entities.Device
import comparisoncreator.solver.Solver
import comparisoncreator.solver.SolverImpl
import comparisoncreator.solver.di.DefaultSolverModule
import dagger.Component
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DefaultSolverModule::class,
    ]
)
interface TestSolverComponent {
    fun getSolver(): Solver
}

fun mockupDevice(properties: MutableMap<String, String>) =
    Device(
        "MockupName",
        "MockupPrice",
        "MockupUrl",
        properties,
    )

class SolverImplTests {
    companion object {
        private val solver: SolverImpl = DaggerTestSolverComponent.create().getSolver() as SolverImpl

        private val testDevices: Collection<Device> = listOf(
            mockupDevice(mutableMapOf(
                "1" to "",
                "2" to "",
                "Y" to "",
                "3" to "",
                "4" to "",
            )
            ),
            mockupDevice(mutableMapOf(
                "1" to "",
                "2" to "",
                "X" to "",
                "3" to "",
                "4" to "",
            )),
            mockupDevice(mutableMapOf(
                "2" to "",
                "3" to "",
                "4" to "",
                "Z" to "",
            )),
        )

    }

    @Test
    fun testSolverImpl() {

        val result = solver.solve(testDevices)

        assertEquals(3, result.size)

        result.forEach {
            assertEquals(3, it.properties.size)
        }

        result.forEach {
            assertTrue(listOf("2", "3", "4").containsAll(it.properties.keys))
        }

    }
}