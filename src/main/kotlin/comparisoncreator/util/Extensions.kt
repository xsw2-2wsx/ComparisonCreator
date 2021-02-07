package comparisoncreator.util

import java.util.*

fun <T> Collection<T>.allPossibleSubcollectionsSized(size: Int): Collection<Collection<T>> {
    if(this.size < size) return emptyList()
    if(this.size == size) return listOf(this)


    var currentSize = this.size
    var result: MutableList<List<T>> = LinkedList(listOf(LinkedList(this)))

    while(currentSize > size) {
        val newResult: MutableList<List<T>> = LinkedList()

        result.forEach {

            for(i in 0 until currentSize) {
                val newCombination = LinkedList(it)
                newCombination.removeAt(i)
                newResult.add(newCombination)
            }

        }
        result = newResult
        currentSize--
    }

    return result
}