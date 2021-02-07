package comparisoncreator.data.di

import dagger.Module
import dagger.Provides

@Module
class XKomUrlModule {

    @Provides
    @XKomBaseUrl
    fun provideXKomUrl(): String = "https://www.x-kom.pl"
}