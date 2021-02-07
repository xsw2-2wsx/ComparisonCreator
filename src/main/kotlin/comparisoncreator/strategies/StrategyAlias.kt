package comparisoncreator.strategies

import comparisoncreator.data.entities.Device
import comparisoncreator.solver.Solver

typealias Strategy = (devices: Collection<Device>, solver: Solver) ->  Collection<Device>