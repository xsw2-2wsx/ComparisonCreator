package comparisoncreator.api.wrappers

abstract class BaseWrapper<out T>(val value: T) {
    override fun toString(): String = value.toString()
}
