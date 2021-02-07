package comparisoncreator.solver

import comparisoncreator.data.entities.Device

/**
 * An interface that describes the method to extract from a [Collection] of [devices][Device] only the parameters that are common
 * among them.
 */
interface Solver {
    /**
     * Extracts from the list of [devices][Device] only the properties that are common among them
     * @param devices a list of [devices][Device] to extract
     * @return a [Collection] of the same [devices][Device] only with common properties
     */
    fun solve(devices: Collection<Device>): Collection<Device>
}