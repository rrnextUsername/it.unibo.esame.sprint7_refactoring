/* Generated by AN DISI Unibo */ 
package it.unibo.fridge

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fridge ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
						solve("consult('fridgeModel.pl')","") //set resVar	
						solve("consult('dataFunctions.pl')","") //set resVar	
						solve("showResourceModel","") //set resVar	
						itunibo.coap.modelResourceCoapFridge.create(myself ,"fridge", 5684 )
						itunibo.coap.client.coapClientFridge.create(myself ,"localhost", 5685, "butler" )
						emit("exposeFood", "exposeFood" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t038",targetState="fridgePipeline",cond=whenEvent("msgFridge"))
					transition(edgeName="t039",targetState="exposePipeline",cond=whenEvent("exposeFood"))
				}	 
				state("fridgePipeline") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(_,_,_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								forward("msgFridge", "msgFridge(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)})" ,"fridge" ) 
						}
					}
					 transition(edgeName="t040",targetState="handleAdd",cond=whenDispatch("msgFridge"))
				}	 
				state("handleAdd") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(aggiungi,NAME,CATEGORY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("aggiungi(frigoInv,${payloadArg(1)},${payloadArg(2)})","") //set resVar	
								solve("assert(done(handleAdd,${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("showResourceModel","") //set resVar	
								solve("inventario(frigoInv,L)","") //set resVar	
								val Inventario = getCurSol("L").toString()
								itunibo.fridge.fridgeResourceModelSupport.updateFridgeModel(myself ,Inventario )
								itunibo.coap.client.coapClientFridge.put( "null"  )
						}
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(_,_,_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("msgFridge", "msgFridge(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)})" ,"fridge" ) 
						}
					}
					 transition(edgeName="t041",targetState="handleRemove",cond=whenDispatch("msgFridge"))
				}	 
				state("handleRemove") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(rimuovi,NAME,CATEGORY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("rimuovi(frigoInv,${payloadArg(1)},${payloadArg(2)})","") //set resVar	
								solve("assert(done(handleRemove,${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("showResourceModel","") //set resVar	
								solve("inventario(frigoInv,L)","") //set resVar	
								val Inventario = getCurSol("L").toString()
								itunibo.fridge.fridgeResourceModelSupport.updateFridgeModel(myself ,Inventario )
								itunibo.coap.client.coapClientFridge.put( "null"  )
						}
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(_,_,_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("msgFridge", "msgFridge(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)})" ,"fridge" ) 
						}
					}
					 transition(edgeName="t042",targetState="handleQuery",cond=whenDispatch("msgFridge"))
				}	 
				state("handleQuery") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(conferma,NAME,CATEGORY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("assert(done(handleQuery,${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								solve("presenza(frigoInv,${payloadArg(1)},${payloadArg(2)})","") //set resVar	
								if(currentSolution.isSuccess()) { itunibo.coap.client.coapClientFridge.put( "present"  )
								 }
								else
								{ itunibo.coap.client.coapClientFridge.put( "absent"  )
								 }
								solve("inventario(frigoInv,L)","") //set resVar	
								val Inventario = getCurSol("L").toString()
								itunibo.fridge.fridgeResourceModelSupport.updateFridgeModel(myself ,Inventario )
						}
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(_,_,_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("msgFridge", "msgFridge(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)})" ,"fridge" ) 
						}
					}
					 transition(edgeName="t043",targetState="handleTest",cond=whenDispatch("msgFridge"))
				}	 
				state("handleTest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("msgFridge(ACTION,NAME,CATEGORY)"), Term.createTerm("msgFridge(null,NAME,CATEGORY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)}))","") //set resVar	
								itunibo.coap.client.coapClientFridge.put( "null"  )
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("exposePipeline") { //this:State
					action { //it:State
						forward("exposeFood", "exposeFood" ,"fridge" ) 
					}
					 transition(edgeName="t044",targetState="exposeFood",cond=whenDispatch("exposeFood"))
				}	 
				state("exposeFood") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("exposeFood"), Term.createTerm("exposeFood"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(received(exposeFood))","") //set resVar	
								solve("inventario(frigoInv,INVENTORY)","") //set resVar	
								solve("inventario(frigoInv,L)","") //set resVar	
								val Inventario = getCurSol("L").toString()
								itunibo.fridge.fridgeResourceModelSupport.updateFridgeModel(myself ,Inventario )
								emit("modelContent", "modelContent(frigo(Inventario))" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
