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
		
				var P = Runtime.getRuntime().exec("echo ledOff.py");
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("$name starts")
						delay(200) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="handleUpdateMode",cond=whenDispatch("update_physical_led_mode"))
				}	 
				state("handleUpdateMode") { //this:State
					action { //it:State
						CommUtils.outgreen("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("update_physical_led_mode(MODE)"), Term.createTerm("update_physical_led_mode(M)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val S = payloadArg(0)
												
												P.destroy();
												
												P = when (S) {
													"blink" 	-> Runtime.getRuntime().exec("echo ledBlink.py")
													"on"		-> Runtime.getRuntime().exec("echo ledOn.py")
													"off" 		-> Runtime.getRuntime().exec("echo ledOff.py")
												    else -> {
												        println("Invalid command: $S") // Or log it, or throw an exception
												        null // Or some other default value for P, depending on its type
												    }
												}
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
