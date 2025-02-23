/* Generated by AN DISI Unibo */ 
package it.unibo.led

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

class Led ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "tcp://broker.hivemq.com" )
						CommUtils.outgreen("$name starts")
						delay(200) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleUpdateLedStatus",cond=whenDispatch("update_led_mode"))
				}	 
				state("handleUpdateLedStatus") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("update_led_mode(MODE)"), Term.createTerm("update_led_mode(MODE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val NEW_MODE = payloadArg(0).toString()
												val NEW_MODE_LOG = "led_status_change_to_$NEW_MODE"
								forward("update_physical_led_mode", "update_physical_led_mode($NEW_MODE)" ,"led_device" ) 
								CommUtils.outgreen("$NEW_MODE_LOG")
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "$NEW_MODE_LOG" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","$NEW_MODE_LOG").toString(), "it.unib0.iss.waste-incinerator-service" )   
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="handleUpdateLedStatus",cond=whenDispatch("update_led_mode"))
				}	 
			}
		}
} 
