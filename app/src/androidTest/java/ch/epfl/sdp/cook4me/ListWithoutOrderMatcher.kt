package ch.epfl.sdp.cook4me

import io.mockk.Matcher
import io.mockk.MockKMatcherScope
import io.mockk.impl.InternalPlatform

// copied from https://mockk.io/
data class ListWithoutOrderMatcher<T>(
    val expectedList: List<T>,
    val refEq: Boolean
) : Matcher<List<T>> {
    val map = buildCountsMap(expectedList)

    override fun match(arg: List<T>?): Boolean {
        if (arg == null) return false
        return buildCountsMap(arg) == map
    }

    private fun buildCountsMap(list: List<T>): Map<Any?, Int> {
        val map = mutableMapOf<Any?, Int>()

        for (item in list) {
            val key = when {
                item == null -> nullKey
                refEq -> InternalPlatform.ref(item)
                else -> item
            }
            map.compute(key) { _, value -> (value ?: 0) + 1 }
        }

        return map
    }

    override fun toString() = "matchListWithoutOrder($expectedList)"

    @Suppress("UNCHECKED_CAST")
    override fun substitute(map: Map<Any, Any>): Matcher<List<T>> {
        return copy(expectedList = expectedList.map { map.getOrDefault(it as Any?, it) } as List<T>)
    }

    companion object {
        val nullKey = Any()
    }
}

inline fun <reified T : List<E>, E : Any> MockKMatcherScope.matchListWithoutOrder(
    vararg items: E,
    refEq: Boolean = true
): T = match(ListWithoutOrderMatcher(listOf(*items), refEq))
