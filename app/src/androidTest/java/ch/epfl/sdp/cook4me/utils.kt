import kotlinx.coroutines.runBlocking

fun assertThrowsAsync(f: suspend () -> Unit) {
    try {
        runBlocking {
            f()
        }
    } catch (
        e: Exception
    ) {
        return
    }
    throw AssertionError("no exception was thrown")
}
