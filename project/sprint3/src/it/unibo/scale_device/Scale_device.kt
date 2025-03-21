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
		
				val MAX_WEIGHT = 250; // aka 5 rp
				var CURRENT_WEIGHT = 250; 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("$name starts")
						delay(500) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="addWeight",cond=whenDispatch("load_weight"))
					transition(edgeName="t03",targetState="removeWeight",cond=whenDispatch("unload_weight"))
				}	 
				state("addWeight") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("load_weight(WEIGHT)"), Term.createTerm("load_weight(WEIGHT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val NEW_WEIGHT = payloadArg(0).toInt()  
								if(  NEW_WEIGHT < MAX_WEIGHT  
								 ){ CURRENT_WEIGHT += NEW_WEIGHT  
								emitLocalStreamEvent("scale_data", "scale_data($CURRENT_WEIGHT)" ) 
								CommUtils.outblack("$name weight added, current = $CURRENT_WEIGHT")
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="addWeight",cond=whenDispatch("load_weight"))
					transition(edgeName="t05",targetState="removeWeight",cond=whenDispatch("unload_weight"))
				}	 
				state("removeWeight") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("unload_weight(WEIGHT)"), Term.createTerm("unload_weight(WEIGHT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CURRENT_WEIGHT -= 50  
								emitLocalStreamEvent("scale_data", "scale_data($CURRENT_WEIGHT)" ) 
								CommUtils.outblack("$name weight removed, current = $CURRENT_WEIGHT")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="addWeight",cond=whenDispatch("load_weight"))
					transition(edgeName="t07",targetState="removeWeight",cond=whenDispatch("unload_weight"))
				}	 
			}
		}
} 
