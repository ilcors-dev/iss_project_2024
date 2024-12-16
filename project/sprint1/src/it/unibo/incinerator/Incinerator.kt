/* Generated by AN DISI Unibo */ 
package it.unibo.incinerator

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

class Incinerator ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var ACTIVE = false;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t011",targetState="startup",cond=whenDispatch("startIncinerator"))
				}	 
				state("startup") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("startIncinerator(0)"), Term.createTerm("startIncinerator(0)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 ACTIVE = true  
								CommUtils.outblack("$name ACTIVATED")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="startBurning",cond=whenDispatch("startBurning"))
				}	 
				state("startBurning") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("startIncinerator(0)"), Term.createTerm("startIncinerator(BTIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var BurnTime = payloadArg(0).toLong()  
								CommUtils.outmagenta("$name - Start burning phase")
								emit("burning", "burning(BurnTime)" ) 
								delay(BurnTime)
								CommUtils.outmagenta("$name - Finished burning RP")
								emit("finishedBurning", "finishedBurning(BurnTime)" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t013",targetState="startBurning",cond=whenDispatch("startBurning"))
				}	 
			}
		}
} 