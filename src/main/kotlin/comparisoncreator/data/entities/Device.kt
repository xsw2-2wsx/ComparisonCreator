package comparisoncreator.data.entities

data class Device(
    val name: String,

    val price: String,

    val url: String,

    val properties: Map<String, String>
)