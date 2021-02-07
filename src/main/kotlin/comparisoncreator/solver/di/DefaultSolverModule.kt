package comparisoncreator.solver.di

import comparisoncreator.solver.Solver
import comparisoncreator.solver.SolverImpl
import dagger.Binds
import dagger.Module

@Module
interface DefaultSolverModule {
    @Binds
    fun bindSolver(s: SolverImpl): Solver
}