package comparisoncreator.strategies

import comparisoncreator.data.entities.Device
import comparisoncreator.solver.Solver

/**
 * Strategy determines how the devices added to the [Comparison][comparisoncreator.Comparison] object should be
 * turned into a comparison result. You can use strategy constructor functions defined in [Strategies] or create
 * you own.
 */
typealias Strategy = (devices: Collection<Device>, solver: Solver) ->  Collection<Device>