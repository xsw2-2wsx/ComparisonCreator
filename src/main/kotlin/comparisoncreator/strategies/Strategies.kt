package comparisoncreator.strategies

import comparisoncreator.exceptions.NotEnoughDevicesException
import comparisoncreator.util.allPossibleSubcollectionsSized

object Strategies {

    fun includeAll(): Strategy = { devices, solver -> solver.solve(devices) }

    fun mostCommonForAmount(amount: Int): Strategy = { devices, solver ->
        devices
            .allPossibleSubcollectionsSized(amount)
            .map { solver.solve(it) }
            .maxByOrNull { it.first().properties.size }
            ?: throw NotEnoughDevicesException(amount, devices.size)
    }

}