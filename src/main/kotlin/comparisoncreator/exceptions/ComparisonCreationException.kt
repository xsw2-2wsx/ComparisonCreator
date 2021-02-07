package comparisoncreator.exceptions

open class ComparisonCreationException : ComparisonCreatorException {
    constructor(msg: String) : super(msg)
    constructor(msg: String, cause: Throwable) : super(msg, cause)
}