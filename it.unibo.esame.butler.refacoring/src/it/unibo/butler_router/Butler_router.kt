/* Generated by AN DISI Unibo */ 
package it.unibo.butler_router

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Butler_router ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t012",targetState="solveAction",cond=whenDispatch("action"))
				}	 
				state("solveAction") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						forward("actionComplete", "actionComplete(ok)" ,"butler_solver" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
