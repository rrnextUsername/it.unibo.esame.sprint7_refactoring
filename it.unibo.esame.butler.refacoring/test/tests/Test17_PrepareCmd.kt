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
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","pantry")
		
		solveCheckGoal(resource!!,"done( handleSwap, pantryInv, butlerInv, piatto, silverware )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","fridge")
		
		solveCheckGoal(resource!!,"done( handleAdd, butlerInv, torta, cibo )")
		solveCheckGoal(fridge_model_handler!!,"done( handleRemove, torta, cibo )")
		
		solveCheckGoal(resource!!,"done( handleAdd, butlerInv, crema, cibo )")
		solveCheckGoal(fridge_model_handler!!,"done( handleRemove, crema, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","table")
		
		solveCheckGoal(resource!!,"done( handleSwap, butlerInv, tableInv, piatto, silverware )")
		
		solveCheckGoal(resource!!,"done( handleSwap, butlerInv, tableInv, torta, cibo )")
		
		solveCheckGoal(resource!!,"done( handleSwap, butlerInv, tableInv, crema, cibo )")
		
		solveCheckGoalOrder(butler_pathfinder_handler!!,"done(movimento, RES)","home")
		
		
		//final state consistent		
		solveCheckGoal(resource!!,"presenza( tableInv, piatto, silverware )")
		solveCheckGoal(resource!!,"presenza( tableInv, torta, cibo )")
		solveCheckGoal(resource!!,"presenza( tableInv, crema, cibo )")
		
		
		solveCheckGoal(resource!!,"presenza( butlerInv, piatto, silverware )","fail")
		solveCheckGoal(resource!!,"presenza( butlerInv, torta, cibo )","fail")
		solveCheckGoal(resource!!,"presenza( butlerInv, crema, cibo )","fail")
		
		
		solveCheckGoal(resource!!,"presenza( pantryInv, piatto, silverware )","fail")
		solveCheckGoal(fridge_model_handler!!,"presenza( frigoInv, torta, cibo )","fail")
		solveCheckGoal(fridge_model_handler!!,"presenza( frigoInv, crema, cibo )","fail")
		
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
