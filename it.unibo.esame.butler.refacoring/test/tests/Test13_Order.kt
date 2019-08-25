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

class TestOrder {
  var butler_solver : ActorBasic? = null
  var butler_pathfinder_handler : ActorBasic? = null
  var butler_test_handler : ActorBasic? = null
  var pathfinder : ActorBasic? = null
	
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
			butler_test_handler = sysUtil.getActor("butler_test_handler")	
			pathfinder = sysUtil.getActor("pathfinder")		
		    println(" %%%%%%% TestPathfinder getActors resource=${butler_solver}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestPathfinder terminate ")
	}
 
	@Test
	fun orderTest() {
		println(" %%%%%%% TestPathfinder  test destinazione ")
		sendCmdMessage(butler_solver!!,40000)
		solveCheckGoal(pathfinder!!,"curPos(0,0)")
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","location1")
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","location2")
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","home")
		solveCheckGoalOrder(butler_test_handler!!,"done(check, RES)","check1")
		solveCheckGoalOrder(butler_test_handler!!,"done(check, RES)","check2")
 	}
//----------------------------------------
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
		actor.scope.launch{
			println("--- sendCmdMessage cmd(testOrder, null)")
  			MsgUtil.sendMsg("cmd","cmd(testOrder,null)",actor)
 		}
		delay(time) //give time to do the move
  	}

	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( goal  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestPathfinder expected 'success' found $result")
	}
 	
	fun solveCheckGoalOrder( actor : ActorBasic, goal : String, expectedSolveResult: String){
		actor.solve( "retract($goal)" )
		var result =  actor.resVar
		var solveResult = actor.getCurSol("RES").toString()
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestPathfinder expected 'success' found $result")
		assertTrue(solveResult==expectedSolveResult,"%%%%%%% TestPathfinder expected $expectedSolveResult found $solveResult")
	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
