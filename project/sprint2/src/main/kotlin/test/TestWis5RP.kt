import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.msg.*
import unibo.basicomm23.utils.*
import unibo.basicomm23.utils.CommUtils.delay
import unibo.basicomm23.utils.ConnectionFactory
import java.util.ArrayList
import kotlin.collections.ArrayDeque

class TestWis5RP {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            it.unibo.ctxwis24_functional_test.main()
			
			delay(2000)
        }
    }

    @Test
    fun testMessageSequence() {
        val brokerUrl = "tcp://broker.hivemq.com"
        val clientId = "testClient_${System.currentTimeMillis()}"
        val persistence = MemoryPersistence()

        val client = MqttClient(brokerUrl, clientId, persistence)
        val connOpts = MqttConnectOptions().apply { isCleanSession = true }
        client.connect(connOpts)
		
	    fun attemptReconnect() {
	      try {
	        println("Attempting to reconnect...")
	        client.connect(connOpts)
	        client.subscribe("it.unib0.iss.waste-incinerator-service")
	        println("Reconnected successfully.")
	      } catch (e: Exception) {
	        println("Reconnect failed: ${e.message}")
	        delay(5000) // Wait before retrying
	        attemptReconnect() // Recursive call to retry
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
                    println("Message in stack: ${messagesStack.first()}")

                    if (msg.msgContent() == messagesStack.first()) {
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

		delay(160000)

        assertEquals(true, testSuccess)
    }
}
