package comparisoncreator.data.di

import comparisoncreator.data.scrapper.Scrapper
import comparisoncreator.data.scrapper.XKomScrapper
import dagger.Binds
import dagger.Module

@Module
interface XKomScrapperModule {
    @Binds
    fun bindScrapper(s: XKomScrapper): Scrapper
}