import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.msg.ProtocolType
import unibo.basicomm23.utils.*
import unibo.basicomm23.utils.CommUtils.delay
import java.io.File
import java.io.IOException

class Test4RP {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            File("wis.log").delete()
            it.unibo.ctxwis24.main()
            delay(2000)
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

        delay(160000)
        assertLogEntry("wis.log", "RPCONT_0")
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
