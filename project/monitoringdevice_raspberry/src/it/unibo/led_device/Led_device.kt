/* Generated by AN DISI Unibo */ 
package it.unibo.led_device

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023

//User imports JAN2024

class Led_device ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var P = Runtime.getRuntime().exec("python3 scripts/ledOff.py");
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "tcp://192.168.0.200" )
						CommUtils.outgreen("$name starts")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="handleUpdateMode",cond=whenDispatch("update_physical_led_mode"))
				}	 
				state("handleUpdateMode") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("update_physical_led_mode(MODE)"), Term.createTerm("update_physical_led_mode(MODE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val S = payloadArg(0)
												val NEW_MODE_LOG = "led_status_change_to_$S"
												
												P.destroy();
												
												P = when (S) {
													"blink" 	-> Runtime.getRuntime().exec("python3 scripts/ledBlink.py")
													"on"		-> Runtime.getRuntime().exec("python3 scripts/ledOn.py")
													"off" 		-> Runtime.getRuntime().exec("python3 scripts/ledOff.py")
												    else -> {
												        println("Invalid command: $S")
												        null
												    }
												}
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "$NEW_MODE_LOG" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","$NEW_MODE_LOG").toString(), "it.unib0.iss.waste-incinerator-service" )   
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="handleUpdateMode",cond=whenDispatch("update_physical_led_mode"))
				}	 
			}
		}
} 
