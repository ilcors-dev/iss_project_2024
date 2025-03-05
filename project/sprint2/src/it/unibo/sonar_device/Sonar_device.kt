/* Generated by AN DISI Unibo */ 
package it.unibo.sonar_device

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

class Sonar_device ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 
				lateinit var reader  : java.io.BufferedReader
			    lateinit var process : Process	
			    var Distance = 60
			    
			    val SENSITIVITY = 1
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "tcp://broker.hivemq.com" )
						CommUtils.outblue("$name starts")
						delay(1000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="handleLoadAsh",cond=whenDispatch("load_ash"))
				}	 
				state("handleLoadAsh") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("load_ash(D)"), Term.createTerm("load_ash(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Distance -= payloadArg(0).toInt()  
								CommUtils.outblack("loaded ash, sonar measuring = $Distance")
								emitLocalStreamEvent("sonar_data", "distance($Distance)" ) 
								 val NEW_ASH_LOG = "ash_level_updated=$Distance"  
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "$NEW_ASH_LOG" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","$NEW_ASH_LOG").toString(), "it.unib0.iss.waste-incinerator-service" )   
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t011",targetState="handleLoadAsh",cond=whenDispatch("load_ash"))
				}	 
			}
		}
} 
