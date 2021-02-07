package comparisoncreator

import comparisoncreator.data.di.XKomBaseUrl
import comparisoncreator.data.di.XKomScrapperModule
import comparisoncreator.data.scrapper.Scrapper
import comparisoncreator.data.scrapper.XKomScrapper
import dagger.Component
import dagger.Module
import dagger.Provides
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import javax.inject.Singleton

@Module
class MockupUrlModule {
    @Provides
    @XKomBaseUrl
    fun getUrl(): String = "test"
}

@Singleton
@Component(
    modules = [
        XKomScrapperModule::class,
        MockupUrlModule::class,
    ]
)
interface TestScrapperComponent {
    fun getScrapper(): Scrapper
}

class ScrapperTests {
    companion object {

        private var scrapper = DaggerTestScrapperComponent.create().getScrapper() as XKomScrapper

    }

    @Test
    fun scrapDeviceFromDocument() {
        val html: String = File("src/test/resources/ProductPage.html").readText()
        val dev = scrapper.scrapDocumentForDevice(Jsoup.parse(html))

        assertTrue(dev.name.isNotBlank())
        assertTrue(dev.price.isNotBlank())

        assertTrue(
            dev.properties.values.map(String::isNotBlank).reduce(Boolean::and)
        )

    }

    @Test
    fun scrapSearchResultsForUrl() {
        val html: String = File("src/test/resources/SearchResult.html").readText()
        val doc = Jsoup.parse(html, "https://www.test.com")

        val urls = scrapper.scrapSearchPageForUrls(doc)

        Assertions.assertEquals(30, urls.size)
        assertTrue(urls.map(String::isNotEmpty).reduce(Boolean::and))
    }

    @Test
    fun scrapMaxPageTest() {
        val html: String = File("src/test/resources/SearchResult.html").readText()
        val doc = Jsoup.parse(html, "https://www.test.com")

        val result = scrapper.scrapMaxPage(doc)

        Assertions.assertEquals(11, result)
    }
    
    

}

