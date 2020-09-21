import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream

class TextFileProcessor(
    inName: String,
    outName: String,
) {

    private val input = FileInputStream(inName).reader()
    private val outStream = FileOutputStream(outName)
    private val output = outStream.writer()

    private fun out(result: Long) {
        output.write(result.toString())
        output.write("\n")
        output.flush()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun clean(throwable: Throwable? = null) {
        output.flush()
        outStream.close()
    }

    /**
     * Main processing function
     */
    private suspend fun processLine(s: String): Long {
        val num = s.toLong()
        delay(num * 1000L)
        return num
    }

    @ExperimentalCoroutinesApi
    fun process() = runBlocking {
        var outJob: Job? = null
        input.forEachLine { line ->
            val deferred = async { processLine(line) }
            val prevOutJob = outJob
            val thisOutJob = launch (start = CoroutineStart.LAZY) { out(deferred.getCompleted()) }
            outJob = thisOutJob
            deferred.invokeOnCompletion {
                if (prevOutJob == null) thisOutJob.start()
                else prevOutJob.invokeOnCompletion {
                    thisOutJob.start()
                }
            }
        }

        outJob?.invokeOnCompletion(::clean) ?: clean()
    }
}