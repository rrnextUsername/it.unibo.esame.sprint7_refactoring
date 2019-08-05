package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.GlobalScope
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import it.unibo.kactor.sysUtil
import org.junit.jupiter.api.AfterEach
import it.unibo.kactor.MsgUtil

class TestDestinationReachedStopAppl {
  var butler_solver : ActorBasic? = null
  var butler_pathfinder_handler : ActorBasic? = null
  var pathfinder : ActorBasic? = null
  var frontend_dummy: ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {
  	 		GlobalScope.launch{ //activate an observer ...
 				itunibo.coap.observer.main()		//blocking
 			}	
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestPathfinder starts fridge mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			butler_solver = sysUtil.getActor("butler_solver")
			butler_pathfinder_handler = sysUtil.getActor("butler_pathfinder_handler")	
			pathfinder = sysUtil.getActor("pathfinder")		
			frontend_dummy = sysUtil.getActor("frontend_dummy")	
		    println(" %%%%%%% TestPathfinder getActors resource=${butler_solver}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestPathfinder terminate ")
	}
 
	@Test
	fun destinationReachedTest() {
		println(" %%%%%%% TestPathfinder  test destinazione ")
		sendCmdMessage(butler_solver!!,2000)
		emitEvent(frontend_dummy!!,"stopAppl",2000)
		emitEvent(frontend_dummy!!,"reactivateAppl",2000)
		delay(5000)
		solveCheckGoal(pathfinder!!,"done(stop)")
		solveCheckGoal(pathfinder!!,"done(restart)")
		solveCheckGoal(pathfinder!!,"curPos(5,0)")
 	}
//----------------------------------------
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
		actor.scope.launch{
			println("--- sendCmdMessage cmd(testDestination, null)")
  			MsgUtil.sendMsg("cmd","cmd(testDestination,null)",actor)
 		}
		delay(time) //give time to do the move
  	}
	
	fun emitEvent(actor : ActorBasic, event: String, time : Long){
		actor.scope.launch{
			println("--- emitEvent $event")
  			actor.emit("$event","$event")
 		}
		delay(time) //give time to do the move
	}

 	
	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( goal  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestPathfinder expected 'success' found $result")
	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
