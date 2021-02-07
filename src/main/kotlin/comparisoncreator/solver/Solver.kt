package comparisoncreator.solver

import comparisoncreator.data.entities.Device

interface Solver {
    fun solve(devices: Collection<Device>): Collection<Device>
}