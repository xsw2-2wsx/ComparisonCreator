# ComparisonCreator

**ComparisonCreator is a kotlin library that allows for creation of comparisons of devices from [Xkom web store](https://www.x-kom.pl).** It has been made in order 
to complete school assigments that involved this activity automatically.

# Usage

The main API entry point is the `Comparison` object. It exposes the methods for fetching devices, allows to set the comparison `Strategy` and stores the devices
to compare (as an implementation of `Set`).

To acquire an instance of the `Comparison`, use one of the `newComparison` methods avaliable from `compation object`.

```
val comparison = Comparison.newComparison()

val device1 = Device(
    name = "device1",
    price = "100",
    url = "https://www.example.com/device1",
    properties = mutableMapOf(
        "p1" to "v1",
        "p2" to "v2",
    ),
)

comparison.add(device1)

val result: Collection<Device> = comparison.create()
```

**The result of a comparison is a collection of `Devices` with common properties.**  
It is recomended to use the second factory method whenever possible in order to leverage more concise API syntax:

```
val result = Comparison.newComparison {
    Device(
        name = "device1",
        price = "100",
        url = "https://www.example.com/device1",
        properties = mutableMapOf(
            "p1" to "v1",
            "p2" to "v2",
        ),
    ).addToComparison()
}
```
Note that in this case the comparison result is returned instead of the `Comparison` instance.

## Scrap API

`Comparison` instance also exposes a scrap API that can be used to fetch devices directly from website.

### Fetching devices from URLs

```
val result = Comparison.newComparison {

    fetchDevice("https://www.example.com/dev1").addToComparison()

    fetchDevices(
        "https://www.example.com/dev2",
        "https://www.example.com/dev3",
        "https://www.example.com/dev4",
    )
        .shuffled()
        .take(2)
        .addToComparison()
}
```

### Fetching devices from search page

```
val result = Comparison.newComparison {
    
    "https://www.example.com/search?q=something"
        .deviceUrls()
        .first()
        .fetchDevice()
        .addToComparison()
    
    SearchPageUrl("https://www.example.com/search?q=somethingElse")
        .deviceUrls()
        .first()
        .fetchDevice()
        .addToComparison()
}
```

### Searching

```
val result = Comparison.newComparison {
    
    "this is search query"
        .search() // Returns a list of search page URLs
        .shuffled()
        .first()
        .deviceUrls()
        .take(5)
        .map(::fetchDevice)
        .addToComparison()
    
    SearchQuery("this is search query too").search()
    
}
```

## Strategy

`Strategy` determines how the comparison is created. You can use one of the factory methods in `Strategies` object to acquire a `Strategy`:

```
val result = Comparison.newComparison {

    strategy = Strategies.mostCommonForAmount(5)

}
```

Or if none suit your needs, implement your own:

```
val result = Comparison.newComparison {

    strategy = { devices, solver ->
        println("Hello from my custom strategy!")
        solver.solve(devices) 
    }

}
```

`devices` parameter is the collection of devices added to this `Comparison`, the `solver` is an instance of the `Solver` interface, which picks out the
common parameters of given devices.  
Default `Strategy` is obtained using `Strategies.includeAll()`
  
## `DeviceUrl`, `SearchPageUrl` and `SearchQuery`

The `DeviceUrl`, `SearchPageUrl` and `SearchQuery` are just wrappers on the `String` that indicate it's purpose and are code completion friendly.


