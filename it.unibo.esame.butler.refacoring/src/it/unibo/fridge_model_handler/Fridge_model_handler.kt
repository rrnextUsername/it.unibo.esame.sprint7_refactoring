/* Generated by AN DISI Unibo */ 
package it.unibo.fridge_model_handler

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fridge_model_handler ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var DoneStatus = "null" 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
						solve("consult('fridgeModel.pl')","") //set resVar	
						solve("consult('dataFunctions.pl')","") //set resVar	
						solve("showResourceModel","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t037",targetState="handleAdd",cond=whenDispatch("fridge_handleAdd"))
					transition(edgeName="t038",targetState="handleRemove",cond=whenDispatch("fridge_handleRemove"))
					transition(edgeName="t039",targetState="handleQuery",cond=whenDispatch("fridge_handleQuery"))
					transition(edgeName="t040",targetState="handleExposeFood",cond=whenDispatch("fridge_handleExposeFood"))
				}	 
				state("handleAdd") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("fridge_handleAdd(NAME,CATEG)"), Term.createTerm("fridge_handleAdd(NAME,CATEG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("aggiungi(frigo,${payloadArg(0)},${payloadArg(1)})","") //set resVar	
								solve("showResourceModel","") //set resVar	
								itunibo.fridge.fridgeResourceModelSupport.exposeFridgeModel(myself)
						}
					}
					 transition( edgeName="goto",targetState="done", cond=doswitch() )
				}	 
				state("handleRemove") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("fridge_handleRemove(NAME,CATEG)"), Term.createTerm("fridge_handleRemove(NAME,CATEG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("rimuovi(frigo,${payloadArg(0)},${payloadArg(1)})","") //set resVar	
								solve("showResourceModel","") //set resVar	
								itunibo.fridge.fridgeResourceModelSupport.exposeFridgeModel(myself)
						}
					}
					 transition( edgeName="goto",targetState="done", cond=doswitch() )
				}	 
				state("handleQuery") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("fridge_handleQuery(NAME,CATEG)"), Term.createTerm("fridge_handleQuery(NAME,CATEG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("presenza(${payloadArg(0)},${payloadArg(1)},frigo)","") //set resVar	
								if(currentSolution.isSuccess()) { DoneStatus = "present" 
								 }
								else
								{ DoneStatus = "absent" 
								 }
						}
					}
					 transition( edgeName="goto",targetState="done", cond=doswitch() )
				}	 
				state("handleExposeFood") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("fridge_handleExposeFood"), Term.createTerm("fridge_handleExposeFood"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("inventario(frigo,INVENTORY)","") //set resVar	
								emit("modelContent", "modelContent(frigo(${getCurSol("INVENTORY").toString()}))" ) 
						}
					}
					 transition( edgeName="goto",targetState="done", cond=doswitch() )
				}	 
				state("done") { //this:State
					action { //it:State
						forward("fridge_done", "fridge_done($DoneStatus)" ,"fridge_cmd_solver" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}