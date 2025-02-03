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
import main.resources.Position

class Wis ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 
				// constants
				val MAX_ASH_CAPACITY = 4; // max 4 rp in ash storage (capacity)
				
				// variables
				var ASHLEVEL  = 0;
				var RPCONT    = 4;
				var INCSTATUS = 0;    // 0 free, 1 busy
				var INHOME    = 0;    // 0 in home, 1 not in home
			
				val LOCATIONS = mapOf(
					"home" 			to Position(0,0),
					"wastein" 		to Position(0,4),
					"burn_in"		to Position(3,2),
					"burn_out"		to Position(5,3),
					"ashout"		to Position(6,4)
				); 
		
				val WASTEIN_POS_X = LOCATIONS["wastein"]?.x;
				val WASTEIN_POS_Y = LOCATIONS["wastein"]?.y;
		
				val HOME_POS_X = LOCATIONS["home"]?.x;
				val HOME_POS_Y = LOCATIONS["home"]?.y;
				
				val BURN_IN_POS_X = LOCATIONS["burn_in"]?.x;
				val BURN_IN_POS_Y = LOCATIONS["burn_in"]?.y;
				
				val BURN_OUT_POS_X = LOCATIONS["burn_out"]?.x;
				val BURN_OUT_POS_Y = LOCATIONS["burn_out"]?.y;
				
				val ASHOUT_POS_X = LOCATIONS["ashout"]?.x;
				val ASHOUT_POS_Y = LOCATIONS["ashout"]?.y;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "tcp://broker.hivemq.com" )
						delay(500) 
						CommUtils.outgreen("$name start")
						//val m = MsgUtil.buildEvent(name, "mqtt_info", "start" ) 
						publish(MsgUtil.buildEvent(name,"mqtt_info","start").toString(), "it.unib0.iss.wis" )   
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
						if(  (RPCONT > 0 && ASHLEVEL < MAX_ASH_CAPACITY && INCSTATUS == 0)  
						 ){request("getrp", "getrp($WASTEIN_POS_X,$WASTEIN_POS_Y)" ,"oprobot" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="printStatus", cond=doswitch() )
				}	 
				state("printStatus") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						
									val status = "INHOME=${INHOME}_RPCONT=${RPCONT}_ASHCONT=${ASHLEVEL}_INCSTATUS=${INCSTATUS}";
									val RP_STATUS = "RPCONT_${RPCONT}";
									val ASHLEVEL_STATUS = "ASHLEVEL_${ASHLEVEL}"
						//val m = MsgUtil.buildEvent(name, "mqtt_info", "$RP_STATUS" ) 
						publish(MsgUtil.buildEvent(name,"mqtt_info","$RP_STATUS").toString(), "it.unib0.iss.wis" )   
						//val m = MsgUtil.buildEvent(name, "mqtt_info", "$ASHLEVEL_STATUS" ) 
						publish(MsgUtil.buildEvent(name,"mqtt_info","$ASHLEVEL_STATUS").toString(), "it.unib0.iss.wis" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="updateIncStatus",cond=whenEvent("burning"))
					transition(edgeName="t01",targetState="updateIncStatus",cond=whenEvent("finishedBurning"))
					transition(edgeName="t02",targetState="moveToBurnIn",cond=whenReply("getrp_status"))
					transition(edgeName="t03",targetState="moveToAshOut",cond=whenReply("extractash_status"))
					transition(edgeName="t04",targetState="updateAshLevel",cond=whenDispatch("ashMeasurement"))
				}	 
				state("moveToBurnIn") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("getrp_status(0)"), Term.createTerm("getrp_status(0)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INHOME = 0  
								 RPCONT -= 1  
								 val STATUS = "update_rp_count_to__${RPCONT}"  
								CommUtils.outgreen("$name - Moving to burn in")
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "$STATUS" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","$STATUS").toString(), "it.unib0.iss.wis" )   
								request("depositrp", "depositrp($BURN_IN_POS_X,$BURN_IN_POS_Y)" ,"oprobot" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t15",targetState="startBurningPhase",cond=whenReply("depositrp_status"))
				}	 
				state("startBurningPhase") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						CommUtils.outgreen("$name - Start burning phase")
						forward("startBurning", "startBurning(9000)" ,"incinerator" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="updateIncStatus",cond=whenEvent("burning"))
				}	 
				state("moveToAshOut") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("extractash_status(0)"), Term.createTerm("extractash_status(0)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INHOME = 0  
								CommUtils.outgreen("$name - Moving to ash out")
								request("depositash", "depositash($ASHOUT_POS_X,$ASHOUT_POS_Y)" ,"oprobot" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t57",targetState="goHome",cond=whenReply("depositash_status"))
				}	 
				state("goHome") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						CommUtils.outgreen("$name - Moving to home")
						request("gohome", "gohome($HOME_POS_X,$HOME_POS_Y)" ,"oprobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t28",targetState="inHome",cond=whenReply("gohome_status"))
				}	 
				state("inHome") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("gohome_status(0)"), Term.createTerm("gohome_status(0)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INHOME = 1  
								CommUtils.outgreen("$name - arrived in home")
								if(  (RPCONT > 0 && ASHLEVEL < MAX_ASH_CAPACITY && INCSTATUS == 0)  
								 ){request("getrp", "getrp($WASTEIN_POS_X,$WASTEIN_POS_Y)" ,"oprobot" )  
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="printStatus", cond=doswitch() )
				}	 
				state("updateIncStatus") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("burning(START_TIME)"), Term.createTerm("burning(START_TIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INCSTATUS = 1  
								CommUtils.outmagenta("$name - start incinerator, update status")
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "incinerator_status_BURNING" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","incinerator_status_BURNING").toString(), "it.unib0.iss.wis" )   
						}
						if( checkMsgContent( Term.createTerm("finishedBurning(TIME_ELAPSED)"), Term.createTerm("finishedBurning(TIME_ELAPSED)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 INCSTATUS = 0  
								CommUtils.outmagenta("$name - finish incinerator, update status")
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "incinerator_status_FINISHED_BURNING" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","incinerator_status_FINISHED_BURNING").toString(), "it.unib0.iss.wis" )   
								request("extractash", "extractash($BURN_OUT_POS_X,$BURN_OUT_POS_Y)" ,"oprobot" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="goHome", cond=doswitchGuarded({ INHOME == 0  
					}) )
					transition( edgeName="goto",targetState="printStatus", cond=doswitchGuarded({! ( INHOME == 0  
					) }) )
				}	 
				state("updateAshLevel") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("ashMeasurement(L)"), Term.createTerm("ashMeasurement(L)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var level = payloadArg(0).toInt()
												ASHLEVEL += level
												val STATUS = "ash_level_to__${ASHLEVEL}"
								CommUtils.outmagenta("$name - Update Ash level")
								//val m = MsgUtil.buildEvent(name, "mqtt_info", "$STATUS" ) 
								publish(MsgUtil.buildEvent(name,"mqtt_info","$STATUS").toString(), "it.unib0.iss.wis" )   
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="printStatus", cond=doswitch() )
				}	 
			}
		}
} 
