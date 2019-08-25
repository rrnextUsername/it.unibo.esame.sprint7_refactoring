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

class TestQueryASync {
  var resource : ActorBasic? = null
  var resource_fridge_handler : ActorBasic? = null
  var resource_fridge : ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {
  	 		GlobalScope.launch{ //activate an observer ...
 				itunibo.coap.observer.main()		//blocking
 			}
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestFridge starts fridge mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			resource = sysUtil.getActor("butler_solver")	
			resource_fridge_handler = sysUtil.getActor("butler_fridge_handler")	
			resource_fridge = sysUtil.getActor("fridge")	
		    println(" %%%%%%% TestFridge getActors resource=${resource} resource_fridge_handler=${resource_fridge_handler} resource_fridge=${resource_fridge}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestFridge terminate ")
	}
 
	@Test
	fun addFoodTest() {
		println(" %%%%%%% TestFridge  queryTest ")
		sendCmdMessage(resource!!,5000)
		solveCheckGoal(resource_fridge_handler!!,"done(actionMsgFridgeSync, _, _, _)")
		solveCheckGoal(resource_fridge!!,"received( _, _, _ )")
 	}
//----------------------------------------
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
		actor.scope.launch{
			println("--- sendCmdMessage cmd(testASync, null)")
  			MsgUtil.sendMsg("cmd","cmd(testASync,null)",actor)
 		}
		delay(time) //give time to do the move
  	}

 	
	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( goal  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestFridge expected 'success' found $result")
	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
