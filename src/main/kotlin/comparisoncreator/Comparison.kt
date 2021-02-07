package comparisoncreator

import comparisoncreator.data.entities.Device
import comparisoncreator.data.scrapper.Scrapper
import comparisoncreator.di.DaggerComparisonComponent
import comparisoncreator.exceptions.ComparisonCreationException
import comparisoncreator.exceptions.ScrappingException
import comparisoncreator.solver.Solver
import comparisoncreator.strategies.Strategy
import javax.inject.Inject
import kotlin.jvm.Throws

/**
 * Main API endpoint, represents a single comparison. **Do not instantiate directly**, use [newComparison] to
 * obtain an instance.
 * The [Collection] is implementation of a Set<Devices> and stores the devices from which the
 * comparison should be created.
 */
class Comparison @Inject constructor(
    var strategy: @JvmSuppressWildcards Strategy,
    private var scrapper: Scrapper,
    private val solver: Solver,
    private val set: MutableSet<Device>,
) : MutableSet<Device> by set {

    companion object {
        private val comparisonFactory = DaggerComparisonComponent.create()

        @JvmStatic
        fun newComparison(): Comparison = comparisonFactory.getComparison()
        fun newComparison(configuration: Comparison.() -> Unit): Collection<Device> =
            newComparison().apply { configuration() }.create()
    }

    /**
     * Searches for a specified [query] and returns a list of URLs to result pages.
     * @param query query to search for
     * @return A collection of URLs to search result pages.
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun search(query: String): Collection<String> = scrapper.search(query)

    /**
     * Searches for a specified query and returns a list of URLs to result pages.
     * @receiver query to search for
     * @return A collection of URLs to search result pages.
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("searchExtension")
    fun String.search(): Collection<String> = search(this)

    /**
     * Retrieves URLs of devices on a search page
     * @param url URL of the search page
     * @return a collection of URLs of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun deviceUrls(url: String): Collection<String> = scrapper.fetchDeviceUrlsFromSearchPage(url)

    /**
     * Retrieves URLs of devices on a search page
     * @receiver URL of the search page
     * @return a collection of URLs of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("deviceUrlsExtension")
    fun String.deviceUrls(): Collection<String> = deviceUrls(this)

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
     * @receiver URL of the product page
     * @return a device
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    @JvmName("fetchDeviceExtension")
    fun String.fetchDevice(): Device = fetchDevice(this)

    /**
     * Scraps the devices from product page URLs
     * @param urls URLs of the product pages
     * @return a collection of devices
     * @throws ScrappingException
     */
    @Throws(ScrappingException::class)
    fun fetchDevices(vararg urls: String): Collection<Device> = urls.toSet().map(::fetchDevice)

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
    fun create(): Collection<Device> = strategy(this, solver)

}