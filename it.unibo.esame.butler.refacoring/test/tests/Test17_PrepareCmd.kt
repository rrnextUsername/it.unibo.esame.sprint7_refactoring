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

class TestPrepareCmd {
  var resource : ActorBasic? = null
  var butler_state_handler : ActorBasic? = null
  var butler_pathfinder_handler : ActorBasic? = null
  var fridge_model_handler : ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {	
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestButtler starts buttler mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			resource = sysUtil.getActor("butler_solver")	
			butler_state_handler = sysUtil.getActor("butler_state_handler")	
			butler_pathfinder_handler = sysUtil.getActor("butler_pathfinder_handler")	
			fridge_model_handler = sysUtil.getActor("fridge_model_handler")	
		    println(" %%%%%%% TestButtler getActors resource=${resource} test_resource=${butler_state_handler}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButtler terminate ")
	}
 
	@Test
	fun addSolveCmd() {
		println(" %%%%%%% TestButtler  solveCmdTest ")
		
		sendCmdMessage(resource!!,30000)
		
		//completeded all tasks
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","pantry")
		
		solveCheckGoal(butler_state_handler!!,"done( handleSwap, pantry, robot, piatto, silverware )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","fridge")
		
		solveCheckGoal(butler_state_handler!!,"done( handleAdd, robot, torta, cibo )")
		solveCheckGoal(fridge_model_handler!!,"done( handleRemove, torta, cibo )")
		
		solveCheckGoal(butler_state_handler!!,"done( handleAdd, robot, crema, cibo )")
		solveCheckGoal(fridge_model_handler!!,"done( handleRemove, crema, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","table")
		
		solveCheckGoal(butler_state_handler!!,"done( handleSwap, robot, table, piatto, silverware )")
		
		solveCheckGoal(butler_state_handler!!,"done( handleSwap, robot, table, torta, cibo )")
		
		solveCheckGoal(butler_state_handler!!,"done( handleSwap, robot, table, crema, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","home")
		
		
		//final state consistent		
		solveCheckGoal(butler_state_handler!!,"presenza( table, piatto, silverware )")
		solveCheckGoal(butler_state_handler!!,"presenza( table, torta, cibo )")
		solveCheckGoal(butler_state_handler!!,"presenza( table, crema, cibo )")
		
		
		solveCheckGoal(butler_state_handler!!,"presenza( robot, piatto, silverware )","fail")
		solveCheckGoal(butler_state_handler!!,"presenza( robot, torta, cibo )","fail")
		solveCheckGoal(butler_state_handler!!,"presenza( robot, crema, cibo )","fail")
		
		
		solveCheckGoal(butler_state_handler!!,"presenza( pantry, piatto, silverware )","fail")
		solveCheckGoal(fridge_model_handler!!,"presenza( frigo, torta, cibo )","fail")
		solveCheckGoal(fridge_model_handler!!,"presenza( frigo, crema, cibo )","fail")
		
		//returned to waitCmd
		solveCheckGoal(resource!!,"stato( waitCmd, _ , _ )")
 	}
	
//----------------------------------------
	 	
	fun solveCheckGoal( actor : ActorBasic, goal : String, expectedResult: String = "success" ){
		actor.solve( "$goal"  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result==expectedResult,"%%%%%%% TestButtler expected '$expectedResult' found $result")
	}
	
	fun solveCheckGoalOrder( actor : ActorBasic, goal : String, expectedSolveResult: String, expectedResult: String = "success"){
		actor.solve( "retract($goal)" )
		var result =  actor.resVar
		var solveResult = actor.getCurSol("RES").toString()
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result== expectedResult,"%%%%%%% TestPathfinder expected '$expectedResult' found $result")
		assertTrue(solveResult==expectedSolveResult,"%%%%%%% TestPathfinder expected $expectedSolveResult found $solveResult")
	}
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
			println("--- sendQueryMessage comando( prepare, null )")
		actor.scope.launch{
			MsgUtil.sendMsg("cmd", "cmd( prepare, null )" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
