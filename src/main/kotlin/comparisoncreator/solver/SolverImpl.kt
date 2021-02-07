package comparisoncreator.solver

import comparisoncreator.data.entities.Device
import javax.inject.Inject

class SolverImpl @Inject constructor() : Solver {
    override fun solve(devices: Collection<Device>): Collection<Device> =
        devices
            .map { it.properties.keys }
            .reduce { acc, value -> value.intersect(acc).toMutableSet() }
            .let { commonProperties ->
                devices.onEach { it.properties = it.properties.filterKeys { commonProperties.contains(it) }.toMutableMap() }
            }
}