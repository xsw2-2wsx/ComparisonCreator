package comparisoncreator.api

import comparisoncreator.api.wrappers.DeviceUrl
import comparisoncreator.api.wrappers.SearchPageUrl
import comparisoncreator.api.wrappers.SearchQuery
import comparisoncreator.data.entities.Device
import comparisoncreator.data.scrapper.Scrapper
import comparisoncreator.di.DaggerComparisonComponent
import comparisoncreator.exceptions.ComparisonCreationException
import comparisoncreator.exceptions.ScrappingException
import comparisoncreator.solver.Solver
import comparisoncreator.strategies.Strategy
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import javax.inject.Inject
import kotlin.jvm.Throws

/**
 * Main API endpoint, represents a single comparison. **Do not instantiate directly**, use [newComparison] to
 * obtain an instance.
 * The [Collection] is implementation of a Set<Devices> and stores the devices from which the
 * comparison should be created.
 */
@Suppress("UNUSED")
class Comparison @Inject constructor(
    var strategy: @JvmSuppressWildcards Strategy,
    private var scrapper: Scrapper,
    private val solver: Solver,
    private val set: MutableSet<Device>,
) : MutableSet<Device> by set {

    companion object {
        private val comparisonFactory = DaggerComparisonComponent.create()

        private val log: Logger = LogManager.getLogger()

        @JvmStatic
        fun newComparison(): Comparison = comparisonFactory.getComparison()

        fun newComparison(configuration: Comparison.() -> Unit): Collection<Device> =
            newComparison().apply { configuration() }.create()
    }

    /**
     * Searches for a specified [query] and returns a list of URLs to result pages.
     * @param query query to search for
     * @return A collection of [SearchPageUrl] representing URLs to search result pages.
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun search(query: String): Collection<SearchPageUrl> = scrapper.search(query).map(::SearchPageUrl)

    /**
     * Searches for a specified [query] and returns a list of URLs to result pages.
     * @param query query to search for
     * @return A collection of [SearchPageUrl] representing URLs to search result pages.
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun search(query: SearchQuery): Collection<SearchPageUrl> = search(query.value)

    /**
     * Searches for a specified query and returns a list of URLs to result pages.
     * @receiver query to search for
     * @return A collection of [SearchPageUrl] representing URLs to search result pages.
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("searchExtension")
    fun String.search(): Collection<SearchPageUrl> = search(this)

    /**
     * Searches for a specified query and returns a list of URLs to result pages.
     * @receiver [SearchQuery] query to search for
     * @return A collection of [SearchPageUrl] representing URLs to search result pages.
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("searchForSearchQueryExtension")
    fun SearchQuery.search(): Collection<SearchPageUrl> = search(value)

    /**
     * Retrieves URLs of devices on a search page
     * @param url URL of the search page
     * @return a collection of [DeviceUrl] representing URLs of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun deviceUrls(url: String): Collection<DeviceUrl> = scrapper.fetchDeviceUrlsFromSearchPage(url).map(::DeviceUrl)

    /**
     * Retrieves URLs of devices on a search page
     * @param url URL of the search page
     * @return a collection of [DeviceUrl] representing URLs of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun deviceUrls(url: SearchPageUrl): Collection<DeviceUrl> = deviceUrls(url.value)

    /**
     * Retrieves URLs of devices on a search page
     * @receiver URL of the search page
     * @return a collection of [DeviceUrl] representing URLs of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("deviceUrlsExtension")
    fun String.deviceUrls(): Collection<DeviceUrl> = deviceUrls(this)

    /**
     * Retrieves URLs of devices on a search page
     * @receiver the url to fetch device URLs from
     * @return a collection of [DeviceUrl] representing URLs of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("deviceUrlsSearchPageUrlExtension")
    fun SearchPageUrl.deviceUrls(): Collection<DeviceUrl> = deviceUrls(value)

    /**
     * Scraps the device from product page
     * @param url URL of the product page
     * @return a device
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun fetchDevice(url: String): Device = scrapper.fetchDevice(url)

    /**
     * Scraps the device from product page
     * @param url URL of the product page
     * @return a device
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun fetchDevice(url: DeviceUrl): Device = fetchDevice(url.value)

    /**
     * Scraps the device from product page
     * @receiver URL of the product page
     * @return a device
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("fetchDeviceExtension")
    fun String.fetchDevice(): Device = fetchDevice(this)

    @Throws(ScrappingException::class)
    @JvmName("fetchDeviceDeviceUrlExtension")
    fun DeviceUrl.fetchDevice(): Device = fetchDevice(value)

    /**
     * Scraps the devices from product page URLs
     * @param urls URLs of the product pages
     * @return a collection of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun fetchDevices(vararg urls: String): Collection<Device> = urls.toSet().map(::fetchDevice)

    /**
     * Scraps the devices from product page URLs
     * @param urls URLs of the product pages
     * @return a collection of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun fetchDevices(vararg urls: DeviceUrl): Collection<Device> = fetchDevices(*urls.map { it.value }.toTypedArray())

    /**
     * Adds specified device to the comparison
     * @receiver device to add
     * @return true if device has been added or false if it is already in this comparison
     */
    fun Device.addToComparison(): Boolean = add(this)

    /**
     * Adds specified devices to the comparison
     * @receiver collection of devices to add
     * @return true if at least one device was added
     */
    fun Collection<Device>.addToComparison(): Boolean = addAll(this)

    /**
     * Specifies the new [Strategy] for this comparison
     * @param newStrategy the new strategy
     */
    fun strategy(newStrategy: Strategy) { strategy = newStrategy }

    /**
     * Creates the comparison
     * @return a [Collection] of devices only with properties common among them
     * @throws ComparisonCreationException
     */
    @Throws(ComparisonCreationException::class)
    fun create(): Collection<Device> {
        log.debug("Executing strategy")
        return strategy(this, solver)
    }

}