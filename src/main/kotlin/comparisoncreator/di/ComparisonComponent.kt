package comparisoncreator.di

import comparisoncreator.Comparison
import comparisoncreator.data.di.XKomScrapperModule
import comparisoncreator.data.di.XKomUrlModule
import comparisoncreator.solver.di.DefaultSolverModule
import comparisoncreator.strategies.di.DefaultStrategyModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        XKomScrapperModule::class,
        DefaultSolverModule::class,
        DefaultStrategyModule::class,
        XKomUrlModule::class,
        LinkedHashSetStorageModule::class,
    ]
)
interface ComparisonComponent {
    fun getComparison(): Comparison
}