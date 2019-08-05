/* Generated by AN DISI Unibo */ 
package it.unibo.fridge_dummy

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fridge_dummy ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('fridgeModel.pl')","") //set resVar	
						solve("consult('dataFunctions.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitMsg", cond=doswitch() )
				}	 
				state("waitMsg") { //this:State
					action { //it:State
					}
					 transition(edgeName="t031",targetState="execute",cond=whenDispatch("msgFridge"))
				}	 
				state("execute") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEG)"), Term.createTerm("msgFridge(aggiungi,NOME,CATEG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
						}
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEG)"), Term.createTerm("msgFridge(rimuovi,NOME,CATEG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
						}
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEG)"), Term.createTerm("msgFridge(conferma,NAME,CATEGORY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								storeCurrentMessageForReply()
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("presenza(${payloadArg(1)},cibo,frigo)","") //set resVar	
								if(currentSolution.isSuccess()) { replyToCaller("replyFridge", "replyFridge(present)")
								 }
								else
								{ replyToCaller("replyFridge", "replyFridge(absent)")
								 }
						}
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEG)"), Term.createTerm("msgFridge(OTHER,NOME,CATEG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
						}
						solve("showResourceModelFridge","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitMsg", cond=doswitch() )
				}	 
			}
		}
}