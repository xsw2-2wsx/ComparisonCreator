package comparisoncreator.data.entities

data class Device(
    var name: String,

    var price: String,

    var url: String,

    var properties: MutableMap<String, String>
)