import java.io.File
import java.io.IOException
import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import unibo.basicomm23.utils.CommUtils.delay

class TestGetRp {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            File("wis.log").delete()
            it.unibo.ctxwis24.main()
            delay(1500)
        }
    }

    @Test
    fun testGetRpCommand() {
        // val conn: Interaction =
        //    ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8121")
        // println("Invio comando getrp al robot")
        // val getRpCommand: IApplMessage =
        //     CommUtils.buildDispatch("testApplication", "getrp", "getrp(0,0)", "oprobot")
        // conn.forward(getRpCommand)

        delay(50000)
        // Chronologically verify log entries
        assertLogEntriesInOrder(
                "wis.log",
                listOf(
                        "moving_to_WasteIn_port",
                        "collected_RP_from_WasteIn_port",
                        "moving_to_BurnIn_port",
                        "deposited_RP_in_BurnIn_port"
                )
        )
    }

    /** Verifies that the given log entries appear in chronological order in the log file. */
    fun assertLogEntriesInOrder(path: String, expectedEntries: List<String>) {
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
