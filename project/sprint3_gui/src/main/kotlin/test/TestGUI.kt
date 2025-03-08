import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class TestGUI {
    // Updated to match the actual CoAP endpoint: coap://localhost:8121/ctxwis24/wis
    private val qakSysHost = "localhost"
    private val ctxportStr = "8121"
    private val qakSysCtx = "ctxwis24"
    private val applActorName = "wis"
    
    private lateinit var mockGuiClient: MockGuiClient
    
    @Before
    fun setup() {
        println("==== TEST SETUP ====")
        println("Connecting to CoAP endpoint: coap://$qakSysHost:$ctxportStr/$qakSysCtx/$applActorName")
        
        // Create a mock GUI client that connects via CoAP
        mockGuiClient = MockGuiClient(qakSysHost, ctxportStr, qakSysCtx, applActorName)
        
        // Check if the WIS service is running
        val isConnected = mockGuiClient.connect()
        println("Connection established: $isConnected")
        assertTrue("Waste Incinerator Service is not running. Please start it before running tests.", 
                  isConnected)
        
        // Give it a moment to establish connection
        println("Waiting for connection to stabilize...")
        CommUtils.delay(1000)
        println("Setup complete.")
    }
    
    @Test
    fun testGuiReceivesUpdatesFromWIS() {
        println("\n==== STARTING TEST: testGuiReceivesUpdatesFromWIS ====")
        
        // Create a latch to wait for updates
        val updateReceived = CountDownLatch(1)
        val receivedCorrectFormat = AtomicBoolean(false)
        
        try {
            println("Setting up observer to listen for updates...")
            // Set up a listener for updates
            mockGuiClient.observeResource(object : UpdateListener {
                override fun onUpdate(message: String) {
                    println("\n>>> Received update: $message")
                    
                    // Check if the message format is correct (semicolon-separated)
                    val isValid = isValidUpdateFormat(message)
                    println("Message format is valid: $isValid")
                    
                    if (isValid) {
                        println("Found correctly formatted message!")
                        receivedCorrectFormat.set(true)
                        updateReceived.countDown()
                    } else {
                        println("Message format is incorrect, continuing to listen...")
                    }
                }
            })
            
            println("Waiting for updates (timeout: 60 seconds)...")
            // Wait for updates (with timeout)
            val received = updateReceived.await(60, TimeUnit.SECONDS)
            
            println("Wait completed. Received valid update: $received")
            
            // Verify that we received updates in the correct format
            assertTrue("Did not receive any updates from WIS within the timeout period", received)
            assertTrue("Received updates but in incorrect format", receivedCorrectFormat.get())
            
        } finally {
            // Clean up
            println("Cleaning up resources...")
            mockGuiClient.disconnect()
            println("Test completed.")
        }
    }
    
    private fun isValidUpdateFormat(message: String): Boolean {
        println("Validating message format...")
        // Check if the message is in the expected semicolon-separated format
        val parts = message.split(";")
        
        println("Message parts (${parts.size}): ${parts.joinToString("\n  ", "\n  ")}")
        
        // Verify we have the expected number of parts
        if (parts.size < 5) {
            println("Invalid: Not enough parts (expected at least 5, got ${parts.size})")
            return false
        }
        
        // Check format of each part (app, rp, incinerator, robot, ash)
        val appMatch = parts[0].trim().startsWith("app:")
        val rpMatch = parts[1].trim().startsWith("rp:")
        val incineratorMatch = parts[2].trim().startsWith("incinerator:")
        val robotMatch = parts[3].trim().startsWith("robot:")
        val ashMatch = parts[4].trim().startsWith("ash:")
        
        println("Validation results:")
        println("  app part: $appMatch (${parts[0].trim()})")
        println("  rp part: $rpMatch (${parts[1].trim()})")
        println("  incinerator part: $incineratorMatch (${parts[2].trim()})")
        println("  robot part: $robotMatch (${parts[3].trim()})")
        println("  ash part: $ashMatch (${parts[4].trim()})")
        
        val isValid = appMatch && rpMatch && incineratorMatch && robotMatch && ashMatch
        println("Overall validation result: $isValid")
        
        return isValid
    }
    
    // Interface for update callbacks
    interface UpdateListener {
        fun onUpdate(message: String)
    }
    
    // Mock client that simulates the GUI's CoAP connection
    class MockGuiClient(
        private val host: String,
        private val port: String,
        private val context: String,
        private val actor: String
    ) {
        private var coapConnection: CoapConnection? = null
        private var observeRelation: org.eclipse.californium.core.CoapObserveRelation? = null
        
        fun connect(): Boolean {
            try {
                // Create a CoAP connection using the constructor directly
                val saddr = "$host:$port"
                val resource = "$context/$actor"
                println("Attempting to connect to: $saddr, resource: $resource")
                
                coapConnection = CoapConnection(saddr, resource)
                println("CoAP connection created successfully: $coapConnection")
                
                // Test the connection with a simple request
                val testResponse = coapConnection!!.request("getState")
                println("Connection test response: $testResponse")
                
                return true
            } catch (e: Exception) {
                println("Failed to connect: ${e.message}")
                e.printStackTrace()
                return false
            }
        }
        
        fun observeResource(listener: UpdateListener) {
            if (coapConnection == null) {
                throw IllegalStateException("Not connected")
            }
            
            println("Setting up CoAP observe relation...")
            
            // Use the actual observe mechanism from CoapConnection
            observeRelation = coapConnection!!.observeResource(object : CoapHandler {
                override fun onLoad(response: CoapResponse) {
                    val responseText = response.responseText
                    println("Received update via observe: $responseText")
                    listener.onUpdate(responseText)
                }
                
                override fun onError() {
                    println("Error in CoAP observation")
                }
            })
            
            println("CoAP observe relation established: $observeRelation")
        }
        
        fun disconnect() {
            println("Disconnecting...")
            
            // Cancel the observe relation if it exists
            if (observeRelation != null) {
                println("Canceling observe relation...")
                coapConnection?.removeObserve(observeRelation!!)
                observeRelation = null
                println("Observe relation canceled")
            }
            
            println("Closing CoAP connection...")
            coapConnection?.close()
            coapConnection = null
            println("Disconnection complete")
        }
    }
}
