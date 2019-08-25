/* Generated by AN DISI Unibo */ 
package it.unibo.dummy_obstacle

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Dummy_obstacle ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitInit", cond=doswitch() )
				}	 
				state("waitInit") { //this:State
					action { //it:State
					}
					 transition(edgeName="t033",targetState="waitStep",cond=whenDispatch("initObstacle"))
				}	 
				state("waitStep") { //this:State
					action { //it:State
					}
					 transition(edgeName="t034",targetState="obstacleAppears",cond=whenDispatch("makingStep"))
					transition(edgeName="t035",targetState="remove",cond=whenDispatch("removeObstacle"))
				}	 
				state("obstacleAppears") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("makingStep(X,Y)"), Term.createTerm("makingStep(3,0)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("assert(done(obstacle,3,0))","") //set resVar	
								emit("obstacle", "obstacle(0)" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitStep", cond=doswitch() )
				}	 
				state("remove") { //this:State
					action { //it:State
						solve("assert(done(removeObstacle,3,0))","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitInit", cond=doswitch() )
				}	 
			}
		}
}
