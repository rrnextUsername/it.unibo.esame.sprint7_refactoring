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

class TestSolveCmd {
  var resource : ActorBasic? = null
  var test_resource : ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {	
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestButtler starts buttler mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			resource = sysUtil.getActor("butler_solver")	
			test_resource = sysUtil.getActor("butler_test_handler")	
		    println(" %%%%%%% TestButtler getActors resource=${resource} test_resource=${test_resource}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButtler terminate ")
	}
 
	@Test
	fun addSolveCmd() {
		println(" %%%%%%% TestButtler  solveCmdTest ")
		
		sendCmdMessageArgs(resource!!,6000)
		
		//completeded all tasks
		solveCheckGoal(test_resource!!,"done( check, 1 )")
		solveCheckGoal(test_resource!!,"done( wait, 1 )")
		solveCheckGoal(test_resource!!,"done( check, 2 )")
		
		//returned to waitCmd
		solveCheckGoal(resource!!,"stato( waitCmd, _ , _ )")
 	}
	
//----------------------------------------
	 	
	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( "retract($goal)"  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestButtler expected 'success' found $result")
	}
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
			println("--- sendQueryMessage comando( testMain )")
		actor.scope.launch{
			MsgUtil.sendMsg("cmd", "cmd( testMain )" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}
	
	fun sendCmdMessageArgs( actor : ActorBasic, time : Long ){
			println("--- sendQueryMessage comando( testMain, null )")
		actor.scope.launch{
			MsgUtil.sendMsg("cmd", "cmd( testMain, null )" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
