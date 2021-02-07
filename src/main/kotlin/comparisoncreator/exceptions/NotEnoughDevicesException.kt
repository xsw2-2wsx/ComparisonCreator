package comparisoncreator.exceptions

class NotEnoughDevicesException(requred: Int, present: Int)
    : ComparisonCrationException("Not enough devices. Required: $requred, present: $present") {
}