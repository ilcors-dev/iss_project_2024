import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.msg.ProtocolType
import unibo.basicomm23.utils.*
import unibo.basicomm23.utils.CommUtils.delay
import java.io.File
import java.io.IOException

class TestGetAsh {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            File("wis.log").delete() // Ensure the log file is clean before running the test
            it.unibo.ctxwis24.main() // Start the system
            delay(2000) // Allow the system to initialize
        }
    }

    @Test
    fun testGetAsh() {
        // val conn: Interaction =
        //    ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8121")

        // val getRpCommand: IApplMessage =
        //     CommUtils.buildDispatch(
        //         "testApplication",
        //         "startIncinerator",
        //         "startIncinerator(100)",
        //         "incinerator",
        //     )
        // conn.forward(getRpCommand)

        delay(50000)

        // Chronologically verify log entries
        assertLogEntriesInOrder(
            "wis.log",
            listOf(
                "moving_to_BurnOut_port",
                "collected_ash_from_BurnOut_port",
                "moving_to_AshOut_port",
                "deposited_ash_in_AshOut_port"
            )
        )
    }

    /**
     * Verifies that the given log entries appear in chronological order in the log file.
     */
    fun assertLogEntriesInOrder(
        path: String,
        expectedEntries: List<String>
    ) {
        try {
            val file = File(path)

            if (!file.exists()) {
                throw IOException("File not found: $path")
            }

            val logLines = file.readLines() // Read all log lines into memory
            var currentIndex = 0

            for (line in logLines) {
                if (currentIndex >= expectedEntries.size) break

                val logContent = line.split(":", limit = 2).getOrNull(1)?.trim() ?: continue

                // Check if the current log line matches the expected log entry
                if (logContent.contains(expectedEntries[currentIndex])) {
                    currentIndex++
                }
            }

            // Ensure all expected log entries were found in the correct order
            assert(currentIndex == expectedEntries.size) {
                "Log entries did not appear in the expected chronological order. Missing entries: ${
                    expectedEntries.subList(currentIndex, expectedEntries.size)
                }"
            }
        } catch (e: Exception) {
            throw AssertionError("Error processing log file: ${e.message}")
        }
    }
}
