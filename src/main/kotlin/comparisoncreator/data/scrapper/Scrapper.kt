package comparisoncreator.data.scrapper

import comparisoncreator.data.entities.Device

interface Scrapper {
    fun fetchDevice(url: String): Device

    //Returns a list of urls of devices
    fun fetchDeviceUrlsFromSearchPage(url: String): List<String>

    // Returns a list of url of search pages
    fun search(query: String): List<String>
}