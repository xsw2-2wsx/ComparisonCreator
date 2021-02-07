package comparisoncreator.exceptions

class NotEnoughDevicesException(requred: Int, present: Int)
    : ComparisonCreationException("Not enough devices. Required: $requred, present: $present") {
}