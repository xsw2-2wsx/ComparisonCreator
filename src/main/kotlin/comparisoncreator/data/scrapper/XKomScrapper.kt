package comparisoncreator.data.scrapper

import comparisoncreator.data.di.XKomBaseUrl
import comparisoncreator.data.entities.Device
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import javax.inject.Inject

class XKomScrapper @Inject constructor(
    @XKomBaseUrl private val xKomBaseUrl: String
): Scrapper {

    companion object {
        private val log: Logger = LogManager.getLogger()
    }

    private fun fetchDocument(url: String): Document = Jsoup.connect(url).get()


    fun scrapDocumentForDevice(doc: Document): Device {
        log.debug("Scrapping the document for device")

        val name = doc.select("h1.sc-1x6crnh-5").text()

        val price = doc.select("div[ class = u7xnnm-4 iVazGO ]").text()

        val properties = HashMap<String, String>()

        val propertyRows: Elements = doc.select("[ class = sc-bwzfXH sc-13p5mv-0 cwztyD sc-htpNat gSgMmi ]")

        propertyRows.forEach {
            val children = it.children()

            properties[children[0].text()] = children[1].text()
        }

        log.debug("Scrapping successful")
        return Device(name, price, doc.baseUri(), properties)
    }

    override fun fetchDevice(url: String): Device = scrapDocumentForDevice(fetchDocument(url))

    fun scrapSearchPageForUrls(doc: Document): List<String> =
        doc
            .select("a[ class = sc-1h16fat-0 dEoadv ]")
            .eachAttr("abs:href")

    override fun fetchDeviceUrlsFromSearchPage(url: String) = scrapSearchPageForUrls(fetchDocument(url))

    fun scrapMaxPage(doc: Document): Int = doc.select("a[ class = sc-1h16fat-0 sc-1xy3kzh-8 cQVHUq ]").last().text().toIntOrNull()?: 1

    override fun search(query: String): List<String> {
        val baseQueryUrl = "$xKomBaseUrl/szukaj?q=$query"
        val doc = fetchDocument(baseQueryUrl)
        val maxPage = scrapMaxPage(doc)

        return generateSequence(1) { it + 1}
            .takeWhile { it <= maxPage }
            .map { "$baseQueryUrl&page=$it" }
            .toList()
    }



}