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

class TestClearCmd {
  var resource : ActorBasic? = null
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
			butler_pathfinder_handler = sysUtil.getActor("butler_pathfinder_handler")	
			fridge_model_handler = sysUtil.getActor("fridge")	
		    println(" %%%%%%% TestButtler getActors resource=${resource}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButtler terminate ")
	}
 
	@Test
	fun addSolveCmd() {
		println(" %%%%%%% TestButtler  solveCmdTest ")
		
		sendCmdMessage(resource!!,60000)
		
		//completeded all tasks
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","table")
		
		solveCheckGoal(resource!!,"done( handleSwap, tableInv, butlerInv, pizza, cibo )")
		solveCheckGoal(resource!!,"done( handleSwap, tableInv, butlerInv, coltello, silverware )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","fridge")
				
		solveCheckGoal(fridge_model_handler!!,"done( handleAdd, pizza, cibo )")
		solveCheckGoal(fridge_model_handler!!,"done( handleAdd, panzerotto, cibo )")
		
		solveCheckGoal(resource!!,"done( handleRemove, butlerInv, pizza, cibo )")
		solveCheckGoal(resource!!,"done( handleRemove, butlerInv, panzerotto, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","dishwasher")
		
		solveCheckGoal(resource!!,"done( handleSwap, butlerInv, dishwasherInv, tazza, silverware )")
		solveCheckGoal(resource!!,"done( handleSwap, butlerInv, dishwasherInv, coltello, silverware )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","home")
		
		//final state consistent		
		solveCheckGoal(resource!!,"presenza( tableInv, _, _ )", "fail")
		solveCheckGoal(resource!!,"presenza( butlerInv, _, _ )", "fail")
		
		
		solveCheckGoal(resource!!,"presenza( dishwasherInv, coltello, silverware )")
		solveCheckGoal(resource!!,"presenza( dishwasherInv, tazza, silverware )")
		
		
		solveCheckGoal(fridge_model_handler!!,"presenza( frigoInv, pizza, cibo )")
		solveCheckGoal(fridge_model_handler!!,"presenza( frigoInv, panzerotto, cibo )")
		
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
			println("--- sendQueryMessage comando( clear, null )")
		actor.scope.launch{
			MsgUtil.sendMsg("cmd", "cmd( clear, null )" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
