package comparisoncreator.exceptions

class ScrappingException : ComparisonCreatorException {
    constructor(msg: String) : super(msg)
    constructor(msg: String, cause: Throwable) : super(msg)
}