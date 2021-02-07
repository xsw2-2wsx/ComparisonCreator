package comparisoncreator.strategies.di

import comparisoncreator.strategies.Strategies
import comparisoncreator.strategies.Strategy
import dagger.Module
import dagger.Provides

@Module
class DefaultStrategyModule {
    @Provides
    fun provideDefaultStrategy(): Strategy = Strategies.includeAll()
}