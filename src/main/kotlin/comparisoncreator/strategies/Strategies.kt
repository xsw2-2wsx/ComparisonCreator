package comparisoncreator.strategies

import comparisoncreator.exceptions.NotEnoughDevicesException
import comparisoncreator.util.allPossibleSubcollectionsSized

/**
 * Defines comparison [Strategy] constructor methods.
 */
object Strategies {

    /**
     * Creates a comparison [Strategy] that includes all the devices in a comparison and
     * finds their common properties
     */
    fun includeAll(): Strategy = { devices, solver -> solver.solve(devices) }

    /**
     * Creates a comparison [Strategy] that finds the combination of [amount] devices with the highest
     * amount of common properties
     */
    fun mostCommonForAmount(amount: Int): Strategy = { devices, solver ->
        devices
            .allPossibleSubcollectionsSized(amount)
            .map { solver.solve(it) }
            .maxByOrNull { it.first().properties.size }
            ?: throw NotEnoughDevicesException(amount, devices.size)
    }

}