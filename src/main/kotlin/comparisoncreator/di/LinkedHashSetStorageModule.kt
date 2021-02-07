package comparisoncreator.di

import comparisoncreator.data.entities.Device
import dagger.Module
import dagger.Provides

@Module
class LinkedHashSetStorageModule {

    @Provides
    fun provideSet(): MutableSet<Device> = LinkedHashSet()
}