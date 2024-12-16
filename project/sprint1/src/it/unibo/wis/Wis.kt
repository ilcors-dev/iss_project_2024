/* Generated by AN DISI Unibo */ 
package it.unibo.wis

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

class Wis ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 
				// constants
				val MAX_ASH_CAPACITY = 4;
				
				// variables
				var ASHLEVEL  = 0;    // max 4 rp in ash storage (capacity)
				var RPCONT    = 0;
				var INCSTATUS = 0;    // 0 free, 1 busy
				var INHOME    = 0;    // 0 in home, 1 not in home
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("$name start")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="setupSystem", cond=doswitch() )
				}	 
				state("setupSystem") { //this:State
					action { //it:State
						CommUtils.outgreen("$name setupping system..")
						forward("startIncinerator", "startIncinerator(0)" ,"incinerator" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="checkStatus", cond=doswitch() )
				}	 
				state("checkStatus") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						delay(500) 
						 
									val status = "INHOME=${INHOME}_RPCONT=${RPCONT}_ASHCONT=${ASHLEVEL}_INCSTATUS=${INCSTATUS}"
									println(status)
									
									if (RPCONT > 0 && ASHLEVEL < MAX_ASH_CAPACITY && INCSTATUS == 0) {
						request("moverobot", "moverobot(X,Y)" ,"oprobot" )  
						 
										INHOME = 1
									} else if (INHOME != 0) {
						request("moverobot", "moverobot(0,0)" ,"oprobot" )  
						 
									}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="updateIncStatus",cond=whenEvent("burning"))
					transition(edgeName="t01",targetState="updateAshLevel",cond=whenDispatch("ashMeasurement"))
				}	 
				state("updateIncStatus") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("burning(START_TIME)"), Term.createTerm("burning(START_TIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INCSTATUS = 1  
								CommUtils.outmagenta("$name - start incinerator, update status")
						}
						if( checkMsgContent( Term.createTerm("finishedBurning(TIME_ELAPSED)"), Term.createTerm("finishedBurning(TIME_ELAPSED)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INCSTATUS = 0  
								CommUtils.outmagenta("$name - finish incinerator, update status")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="updateIncStatus",cond=whenEvent("burning"))
					transition(edgeName="t03",targetState="updateIncStatus",cond=whenEvent("finishedBurning"))
					transition(edgeName="t04",targetState="updateAshLevel",cond=whenDispatch("ashMeasurement"))
				}	 
				state("updateAshLevel") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("ashMeasurement(L)"), Term.createTerm("ashMeasurement(L)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var level = payloadArg(0).toInt()
												ASHLEVEL += level
								CommUtils.outmagenta("$name - Update Ash level")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="updateIncStatus",cond=whenEvent("burning"))
					transition(edgeName="t06",targetState="updateIncStatus",cond=whenEvent("finishedBurning"))
					transition(edgeName="t07",targetState="updateAshLevel",cond=whenDispatch("ashMeasurement"))
				}	 
			}
		}
} 
