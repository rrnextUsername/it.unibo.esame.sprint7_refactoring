/* Generated by AN DISI Unibo */ 
package it.unibo.butler_fridge_handler

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Butler_fridge_handler ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
					}
					 transition(edgeName="t014",targetState="actionPipeline",cond=whenDispatch("action"))
					transition(edgeName="t015",targetState="replyPipeline",cond=whenDispatch("replyFridge"))
				}	 
				state("actionPipeline") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("action(ARG0,ARG1,ARG2,ARG3,ARG4)"), Term.createTerm("action(_,_,_,_,_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								forward("action", "action(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)},${payloadArg(3)},${payloadArg(4)})" ,"butler_fridge_handler" ) 
						}
					}
					 transition(edgeName="t016",targetState="handleMsgFridge",cond=whenDispatch("action"))
				}	 
				state("handleMsgFridge") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("action(ARG0,ARG1,ARG2,ARG3,ARG4)"), Term.createTerm("action(notificaFrigo,AZIONE,NOME,CATEGORIA,_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("retract(currentFood(_))","") //set resVar	
								solve("assert(currentFood(${payloadArg(2)}))","") //set resVar	
								itunibo.coap.client.coapClientButler.put( "${payloadArg(1)}, ${payloadArg(2)}, ${payloadArg(3)}"  )
								solve("assert(done(actionMsgFridgeSync,${payloadArg(1)},${payloadArg(2)},${payloadArg(3)}))","") //set resVar	
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("replyPipeline") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("replyFridge(STATUS)"), Term.createTerm("replyFridge(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("replyFridge", "replyFridge(${payloadArg(0)})" ,"butler_fridge_handler" ) 
								println("$name in ${currentState.stateName} | $currentMsg")
						}
					}
					 transition(edgeName="t017",targetState="handleReplyPresent",cond=whenDispatch("replyFridge"))
				}	 
				state("handleReplyPresent") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("replyFridge(STATUS)"), Term.createTerm("replyFridge(present)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("actionComplete", "actionComplete(ok)" ,"butler_solver" ) 
								println("$name in ${currentState.stateName} | $currentMsg")
						}
						if( checkMsgContent( Term.createTerm("replyFridge(STATUS)"), Term.createTerm("replyFridge(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("replyFridge", "replyFridge(${payloadArg(0)})" ,"butler_fridge_handler" ) 
								println("$name in ${currentState.stateName} | $currentMsg")
						}
					}
					 transition(edgeName="t018",targetState="handleReplyNull",cond=whenDispatch("replyFridge"))
				}	 
				state("handleReplyNull") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("replyFridge(STATUS)"), Term.createTerm("replyFridge(null)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("actionComplete", "actionComplete(ok)" ,"butler_solver" ) 
								println("$name in ${currentState.stateName} | $currentMsg")
						}
						if( checkMsgContent( Term.createTerm("replyFridge(STATUS)"), Term.createTerm("replyFridge(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("replyFridge", "replyFridge(${payloadArg(0)})" ,"butler_fridge_handler" ) 
								println("$name in ${currentState.stateName} | $currentMsg")
						}
					}
					 transition(edgeName="t019",targetState="handleReplyAbsent",cond=whenDispatch("replyFridge"))
				}	 
				state("handleReplyAbsent") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("replyFridge(STATUS)"), Term.createTerm("replyFridge(absent)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("currentFood(CIBO)","") //set resVar	
								val Cibo= getCurSol("CIBO").toString()
								itunibo.butler.butlerResourceModelSupport.emitMissingFood(myself ,Cibo )
								forward("actionComplete", "actionComplete(fail)" ,"butler_solver" ) 
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
