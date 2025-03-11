import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.junit.Assert.assertEquals
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.msg.*
import unibo.basicomm23.utils.*
import unibo.basicomm23.utils.CommUtils.delay
import unibo.basicomm23.utils.ConnectionFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayDeque
import kotlin.concurrent.thread

class TestWis5RP {
    companion object {
        // list to keep track of all started processes
        private val runningProcesses = CopyOnWriteArrayList<Process>()
        
        @BeforeClass
        @JvmStatic
        fun setup() {
            // start the required processes using Gradle
            startGradleProcesses()
            delay(2000)
        }
        
        @AfterClass
        @JvmStatic
        fun tearDown() {
            // kill all processes when test is done
            killAllProcesses()
        }
        
        private fun startGradleProcesses() {
            try {
                // Start monitoring device
                startProcessWithOutput("Monitoring Device", "./gradlew", "runMonitoringDevice")
                delay(2000)
                
                // Start weighing device
                startProcessWithOutput("Weighing Device", "./gradlew", "runWeighingDevice")
                delay(2000)

                // Start the main context
                startProcessWithOutput("Main Context", "./gradlew", "run")
                delay(2000)
            } catch (e: Exception) {
                println("Error starting Gradle processes: ${e.message}")
                e.printStackTrace()
                // Make sure to kill any processes that did start
                killAllProcesses()
            }
        }
        
        private fun startProcessWithOutput(name: String, vararg command: String): Process {
            val processBuilder = ProcessBuilder(*command)
            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true)
            
            val process = processBuilder.start()
            runningProcesses.add(process)
            
            // Start a thread to read and print the output
            thread(start = true) {
                BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        println("[$name] $line")
                    }
                }
            }
            
            println("Started $name process")
            return process
        }
        
        private fun killAllProcesses() {
            println("Shutting down all processes...")
            runningProcesses.forEach { process ->
                try {
                    if (process.isAlive) {
                        process.destroy()
                        // give it a moment to shut down gracefully
                        delay(500)
                        // if it's still alive, force kill it
                        if (process.isAlive) {
                            process.destroyForcibly()
                        }
                    }
                } catch (e: Exception) {
                    println("Error killing process: ${e.message}")
                }
            }
            runningProcesses.clear()
            println("All processes have been terminated")
        }
    }

    @Test
    fun testMessageSequence() {
        val brokerUrl = "tcp://localhost"
        val clientId = "testClient_${System.currentTimeMillis()}"
        val persistence = MemoryPersistence()

        val client = MqttClient(brokerUrl, clientId, persistence)
        val connOpts = MqttConnectOptions().apply {
			isCleanSession = true
		    keepAliveInterval = 30  // seconds
		    connectionTimeout = 60  // seconds
		}
        client.connect(connOpts)
        
		fun attemptReconnect(maxRetries: Int = 5) {
		    var retries = 0
		    while (!client.isConnected && retries < maxRetries) {
		        try {
		            println("Attempting to reconnect... (${retries + 1}/$maxRetries)")
		            client.connect(connOpts)
		            client.subscribe("it.unib0.iss.waste-incinerator-service")
		            println("Reconnected successfully.")
		            break
		        } catch (e: Exception) {
		            println("Reconnect failed: ${e.message}")
		            retries++
		            if (retries < maxRetries) {
		                delay(5000) // Wait before retrying
		            }
		        }
		    }
		    
		    if (!client.isConnected) {
		        println("Failed to reconnect after $maxRetries attempts")
		    }
		}

        var testSuccess = false
        val receivedMessages = ArrayList<String>()
        val messagesStack =
            ArrayDeque(
                listOf(
                    "ASHLEVEL_35",
                    "ASHLEVEL_10", // aka ash storage almost full
                    "led_status_change_to_blink",
                    "ASHLEVEL_0",  // aka ash storage full
                ),
            )

        client.setCallback(
            object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    println("Connection lost: ${cause?.message}")
                    attemptReconnect()
                }

                override fun messageArrived(
                    topic: String?,
                    message: MqttMessage?,
                ) {
                    val payload = message?.toString() ?: ""
                    println("Received on $topic: $payload")
                    val msg = ApplMessage(payload)
                    println("Message content: ${msg.msgContent()}")
                    println("Message in stack: ${messagesStack.firstOrNull() ?: "empty stack"}")

                    if (messagesStack.isNotEmpty() && msg.msgContent() == messagesStack.first()) {
                        receivedMessages.add(msg.msgContent())
                        messagesStack.removeFirst()
                    }

                    if (messagesStack.isEmpty()) {
                        println("Received all messages.")
                        testSuccess = true
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            },
        )

        client.subscribe("it.unib0.iss.waste-incinerator-service")

        try {
            // wait for test to complete or timeout
            val timeout = 160000L
            val startTime = System.currentTimeMillis()
            while (!testSuccess && System.currentTimeMillis() - startTime < timeout) {
                delay(1000)
            }
            
            assertEquals(true, testSuccess)
        } finally {
            // Clean up MQTT client
            try {
                client.disconnect()
                client.close()
            } catch (e: Exception) {
                println("Error closing MQTT client: ${e.message}")
            }
        }
    }
}
