/* Generated by AN DISI Unibo */ 
package it.unibo.oprobot

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

class Oprobot ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var OWNER = "$name";
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("$name STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="engage", cond=doswitch() )
				}	 
				state("engage") { //this:State
					action { //it:State
						CommUtils.outgreen("$name request engage")
						request("engage", "engage($OWNER,350)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t013",targetState="inHome",cond=whenReply("engagedone"))
					transition(edgeName="t014",targetState="handleEngageRefused",cond=whenReply("engagerefused"))
				}	 
				state("handleEngageRefused") { //this:State
					action { //it:State
						CommUtils.outblack("engage $name refused, re-trying after some delay..")
						delay(500) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="engage", cond=doswitch() )
				}	 
				state("execGoHome") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("gohome(TARGETX,TARGETY)"), Term.createTerm("gohome(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								CommUtils.outgreen("$name - Moving to ($X,$Y)")
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								delay(2000) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t015",targetState="inHome",cond=whenReply("moverobotdone"))
					transition(edgeName="t016",targetState="execGoHome",cond=whenReply("moverobotfailed"))
				}	 
				state("inHome") { //this:State
					action { //it:State
						CommUtils.outgreen("$name - waiting in home...")
						forward("robotpositioninfo", "robotpositioninfo(0,0)" ,"wis" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="execGetRp",cond=whenRequest("getrp"))
					transition(edgeName="t018",targetState="execExtractAsh",cond=whenRequest("extractash"))
				}	 
				state("execGetRp") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("getrp(TARGETX,TARGETY)"), Term.createTerm("getrp(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								CommUtils.outgreen("$name - Moving to ($X,$Y)")
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								delay(2000) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t019",targetState="getRpOk",cond=whenReply("moverobotdone"))
					transition(edgeName="t020",targetState="execGetRp",cond=whenReply("moverobotfailed"))
				}	 
				state("getRpOk") { //this:State
					action { //it:State
						delay(500) 
						answer("getrp", "getrp_status", "getrp_status(0)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t121",targetState="execDepositRp",cond=whenRequest("depositrp"))
				}	 
				state("execDepositRp") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("depositrp(TARGETX,TARGETY)"), Term.createTerm("depositrp(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								CommUtils.outgreen("$name - Depositing RP in ($X, $Y)")
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								delay(2000) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t222",targetState="depositRpOk",cond=whenReply("moverobotdone"))
					transition(edgeName="t223",targetState="execDepositRp",cond=whenReply("moverobotfailed"))
				}	 
				state("depositRpOk") { //this:State
					action { //it:State
						delay(500) 
						answer("depositrp", "depositrp_status", "depositrp_status(0)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t324",targetState="execGoHome",cond=whenRequest("gohome"))
				}	 
				state("execExtractAsh") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("extractash(TARGETX,TARGETY)"), Term.createTerm("extractash(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								                var X = payloadArg(0).toInt()
								                var Y = payloadArg(1).toInt()
								CommUtils.outgreen("$name - Moving to ($X,$Y)")
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								delay(2000) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t025",targetState="extractAshOk",cond=whenReply("moverobotdone"))
					transition(edgeName="t026",targetState="execExtractAsh",cond=whenReply("moverobotfailed"))
				}	 
				state("extractAshOk") { //this:State
					action { //it:State
						answer("extractash", "extractash_status", "extractash_status(0)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t027",targetState="execDepositAsh",cond=whenRequest("depositash"))
				}	 
				state("execDepositAsh") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("depositash(TARGETX,TARGETY)"), Term.createTerm("depositash(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								CommUtils.outgreen("$name - Depositing Ash in ($X, $Y)")
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								delay(2000) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t028",targetState="depositAshOk",cond=whenReply("moverobotdone"))
					transition(edgeName="t029",targetState="execDepositAsh",cond=whenReply("moverobotfailed"))
				}	 
				state("depositAshOk") { //this:State
					action { //it:State
						answer("depositash", "depositash_status", "depositash_status(0)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t030",targetState="execGoHome",cond=whenRequest("gohome"))
				}	 
			}
		}
} 
