package comparisoncreator.exceptions

open class ComparisonCreatorException : Exception {
    constructor(msg: String) : super(msg)
    constructor(msg: String, cause: Throwable) : super(msg, cause)
}