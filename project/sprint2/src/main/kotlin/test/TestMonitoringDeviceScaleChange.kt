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

class TestMonitoringDeviceScaleChange {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            it.unibo.ctxscale_tests.main()
			
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

        var testSuccess = false
        val receivedMessages = ArrayList<String>()
        val messagesStack =
            ArrayDeque(
                listOf(
                	"rp_in_waste_storage_1",
					"rp_in_waste_storage_2",
                ),
            )

        client.setCallback(
            object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    println("Connection lost: ${cause?.message}")
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
		
		val conn: Interaction =
            ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8123")
		
		delay(100)

        val loadRp: IApplMessage =
             CommUtils.buildDispatch(
                 "testApplication",
                 "load_weight",
                 "load_weight(50)",
                 "scale_device",
             )
        conn.forward(loadRp)
		
		delay(2000)
		
        conn.forward(loadRp)
	
		delay(5000)

        assertEquals(true, testSuccess)
    }
}
