import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.msg.ProtocolType
import unibo.basicomm23.utils.*
import unibo.basicomm23.utils.CommUtils.delay
import java.io.File
import java.io.IOException

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
        assertLogEntry("wis.log", "moving_to_WasteIn_port")
        assertLogEntry("wis.log", "collected_RP_from_WasteIn_por")
        assertLogEntry("wis.log", "moving_to_BurnIn_port")
        assertLogEntry("wis.log", "deposited_RP_in_BurnIn_port")
    }

    fun assertLogEntry(
        path: String,
        check: String,
    ) {
        try {
            val regex = Regex(check)
            val file = File(path)

            if (!file.exists()) {
                throw IOException("File not found: $path")
            }

            file.bufferedReader().use { reader ->
                var found = false
                reader.forEachLine { line ->
                    val parts = line.split(":", limit = 2)

                    if (parts.size < 2) return@forEachLine // Skip lines without the delimiter

                    val content = parts[1].trim() // content after the '|'

                    if (regex.containsMatchIn(content)) {
                        found = true
                    }
                }
                assert(found) { "Expected log entry matching '$check' not found in the file." }
            }
        } catch (e: Exception) {
            println("Error reading file: ${e.message}")
        }
    }
}
