import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

@ExperimentalCoroutinesApi
class SimpleTest {

    @Test
    fun test() {
        val testDataFolder = "src/test/testData"
        val inputName = "$testDataFolder/in.txt"
        val outputName = "$testDataFolder/out.txt"

        val processor = TextFileProcessor(inputName, outputName)
        processor.process()

        val expected = File(inputName).readText()
        val actual = File(outputName).readText()
        assertEquals(expected, actual)
    }
}