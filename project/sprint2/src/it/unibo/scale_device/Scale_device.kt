/* Generated by AN DISI Unibo */ 
package it.unibo.scale_device

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

class Scale_device ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 CURRENT_WEIGHT = 0  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name starts")
						delay(1000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t032",targetState="addWeight",cond=whenDispatch("load_weight"))
					transition(edgeName="t033",targetState="removeWeight",cond=whenDispatch("unload_weight"))
				}	 
				state("addWeight") { //this:State
					action { //it:State
						 CURRENT_WEIGHT += 50  
						emitLocalStreamEvent("scale_data", "scale_data($CURRENT_WEIGHT)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t034",targetState="addWeight",cond=whenDispatch("load_weight"))
					transition(edgeName="t035",targetState="removeWeight",cond=whenDispatch("unload_weight"))
				}	 
				state("removeWeight") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("unload_weight(WEIGHT)"), Term.createTerm("unload_weight(WEIGHT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CURRENT_WEIGHT -= 50  
								emitLocalStreamEvent("scale_data", "scale_data($CURRENT_WEIGHT)" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t036",targetState="addWeight",cond=whenDispatch("load_weight"))
					transition(edgeName="t037",targetState="removeWeight",cond=whenDispatch("unload_weight"))
				}	 
			}
		}
} 
