/* Generated by AN DISI Unibo */ 
package it.unibo.wisloggerobserver

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

class Wisloggerobserver ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val logger = main.kotlin.Logger.getInstance("wis.log")
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name STARTS in wis")
						observeResource("localhost","8121","ctxwis24","wis","info")
						observeResource("localhost","8121","ctxwis24","oprobot","info")
						observeResource("localhost","8121","ctxwis24","incinerator","info")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t029",targetState="handleInfo",cond=whenDispatch("info"))
				}	 
				state("handleInfo") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("info(SEARCH,TERM)"), Term.createTerm("info(SOURCE,TERM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								        	val Source = payloadArg(0)
									        val infoMsg = payloadArg(1)
									        val M      = "$infoMsg from $Source"
								 logger.log( M )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t030",targetState="handleInfo",cond=whenDispatch("info"))
				}	 
			}
		}
} 
