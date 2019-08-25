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

class TestAddFoodPresentCmd {
  var resource : ActorBasic? = null
  var butler_pathfinder_handler : ActorBasic? = null
  var fridge_model_handler : ActorBasic? = null
  var frontend_dummy : ActorBasic? = null
	
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
			frontend_dummy = sysUtil.getActor("frontend_dummy")	
		    println(" %%%%%%% TestButtler getActors resource=${resource}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButtler terminate ")
	}	
 
	@Test
	fun addSolveCmdPresent() {
		println(" %%%%%%% TestButtler  solveCmdTest Present")
		
		sendCmdMessage(resource!!,40000)
		
		//completeded all tasks
		solveCheckGoal(fridge_model_handler!!,"done( handleQuery, panuozzi, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","fridge")
		
		solveCheckGoal(resource!!,"done( handleAdd, butlerInv, panuozzi, cibo )")
		solveCheckGoal(fridge_model_handler!!,"done( handleRemove, panuozzi, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","table")
		
		solveCheckGoal(resource!!,"done( handleSwap, butlerInv, tableInv, panuozzi, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","home")
		
		
		//final state consistent		
		solveCheckGoal(resource!!,"presenza( tableInv, panuozzi, cibo )")
		
		solveCheckGoal(resource!!,"presenza( butlerInv, panuozzi, cibo )","fail")
		
		solveCheckGoal(fridge_model_handler!!,"presenza( frigoInv, panuozzi, cibo )","fail")
		
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
			println("--- sendQueryMessage comando( addFood, panuozzi )")
		actor.scope.launch{
			MsgUtil.sendMsg("cmd", "cmd( addFood, panuozzi )" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
