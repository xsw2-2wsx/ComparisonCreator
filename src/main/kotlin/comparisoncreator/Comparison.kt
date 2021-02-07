package comparisoncreator

import comparisoncreator.data.entities.Device
import comparisoncreator.data.scrapper.Scrapper
import comparisoncreator.di.DaggerComparisonComponent
import comparisoncreator.exceptions.ScrappingException
import comparisoncreator.solver.Solver
import comparisoncreator.strategies.Strategy
import javax.inject.Inject
import kotlin.jvm.Throws

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

    @Throws(ScrappingException::class)
    fun search(query: String): List<String> = scrapper.search(query)

    @Throws(ScrappingException::class)
    @JvmName("searchExtension")
    fun String.search(): List<String> = search(this)

    @Throws(ScrappingException::class)
    fun deviceUrls(url: String): List<String> = scrapper.fetchDeviceUrlsFromSearchPage(url)

    @Throws(ScrappingException::class)
    @JvmName("deviceUrlsExtension")
    fun String.deviceUrls(): List<String> = deviceUrls(this)

    @Throws(ScrappingException::class)
    fun fetchDevice(url: String): Device = scrapper.fetchDevice(url)

    @Throws(ScrappingException::class)
    @JvmName("fetchDeviceExtension")
    fun String.fetchDevice(): Device = fetchDevice(this)

    @Throws(ScrappingException::class)
    fun fetchDevices(vararg urls: String): Collection<Device> = urls.toList().map(::fetchDevice)

    fun Device.addToComparison(): Boolean = add(this)

    fun Collection<Device>.addToComparison(): Boolean = addAll(this)

    fun strategy(newStrategy: Strategy) { strategy = newStrategy }

    fun create(): Collection<Device> = strategy(this, solver)

}