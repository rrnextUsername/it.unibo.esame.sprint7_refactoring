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

class TestConsistencyOnRestartAppl {
  var resource : ActorBasic? = null
  var resource_test : ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestButtler starts buttler mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			resource = sysUtil.getActor("butler_solver")	
			resource_test = sysUtil.getActor("butler_test_handler")	
		    println(" %%%%%%% TestButtler getActors resource=${resource} resource_test=${resource_test}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButtler terminate ")
	}
 
	@Test
	fun startInWaitTest() {
		println(" %%%%%%% TestButtler  consistencyOnRestartApplTest ")
		sendCmdMessage(resource!!,10)
		
		//test stop
		sendStopApplMessage(resource!!,4000)
		solveCheckGoal(resource!!,"stato( stoppedSolvedAction, _ , _ )")
		
		//test restart
		sendRestartApplMessage(resource!!,3000)
		solveCheckGoal(resource!!,"done( restartSolvedAction)")
		delay(10000)
		
		//test all done in order
		solveCheckGoalOrder(resource_test!!,"done( check, NUMBER )","1")
		solveCheckGoalOrder(resource_test!!,"done( wait, NUMBER )","1")
		solveCheckGoalOrder(resource_test!!,"done( check, NUMBER )","2")
		
		//test no others done
		solveCheckGoalNoOthers(resource_test!!,"done( check, _ )")
		solveCheckGoalNoOthers(resource_test!!,"done( wait, _ )")
 	}
//----------------------------------------
	
	fun solveCheckGoalOrder( actor : ActorBasic, goal : String, NUMBER: String ){
		actor.solve( "retract( $goal )", "NUMBER" )
		
		var result =  actor.resVar
		
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="$NUMBER","%%%%%%% TestButtler expected '$NUMBER' found $result")
	}
	
	fun solveCheckGoalNoOthers(actor : ActorBasic, goal : String){
		actor.solve( "retract($goal)"  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="fail","%%%%%%% TestButtler expected 'fail' found $result")
	}
	
	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( "retract($goal)"  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestButtler expected 'success' found $result")
	}
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
			println("--- sendQueryMessage comando( testMain, _ )")
		actor.scope.launch{
			MsgUtil.sendMsg("cmd", "cmd( testMain, _ )" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}
	
	fun sendStopApplMessage( actor : ActorBasic, time : Long ){
			println("--- sendQueryMessage stopAppl")
		actor.scope.launch{
			MsgUtil.sendMsg("stopAppl", "stopAppl" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}
	
	fun sendRestartApplMessage( actor : ActorBasic, time : Long ){
			println("--- sendQueryMessage reactivateAppl")
		actor.scope.launch{
			MsgUtil.sendMsg("reactivateAppl", "reactivateAppl" ,actor ) 
 		}
		delay(time) //give time to do the move
  	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
