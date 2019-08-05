package tests

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.GlobalScope
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import it.unibo.kactor.sysUtil
import org.junit.jupiter.api.AfterEach
import it.unibo.kactor.MsgUtil
  
class TestPositiveReply {
	var resource: ActorBasic? = null
	var resource_fridge_handler: ActorBasic? = null
	var resource_test_handler: ActorBasic? = null
	var resource_fridge_dummy: ActorBasic? = null
	var resource_front_end_dummy: ActorBasic? = null
	var fridge_model_handler: ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {
  	 		GlobalScope.launch{ //activate an observer ...
 				itunibo.coap.observer.main()		//blocking
 			}	
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestButler starts fridge mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			resource = sysUtil.getActor("butler_solver")
			resource_fridge_handler = sysUtil.getActor("butler_fridge_handler")
			resource_test_handler = sysUtil.getActor("butler_test_handler")
			resource_fridge_dummy = sysUtil.getActor("fridge_cmd_solver")
			resource_front_end_dummy = sysUtil.getActor("frontend_dummy")
			fridge_model_handler = sysUtil.getActor("fridge_model_handler") 
			println(" %%%%%%% TestButler getActors resource=${resource} resource_fridge_handler=${resource_fridge_handler} resource_fridge_dummy=${resource_fridge_dummy} resource_front_end_dummy=${resource_front_end_dummy} resource_test_handler=${resource_test_handler}")
	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButler terminate ")
	}
 
	@Test
	fun queryFoodTest() {
		println(" %%%%%%% TestButler  queryFoodTest with food present")
		
		solveCheckGoal(fridge_model_handler!!, "presenza( frigo, pasta, cibo )")
		sendCmdMessage(resource!!,"pasta",5000)
		
		solveCheckGoal(resource_fridge_handler!!,"done(actionMsgFridgeSync, conferma, pasta, cibo )")
		solveCheckGoal(resource_fridge_handler!!,"currentFood(pasta)")
		
		solveCheckGoal(resource_fridge_dummy!!, "received( conferma, pasta, cibo )")
		
		solveCheckGoal(resource_test_handler!!, "done(check, 1)")
		
		solveCheckGoalFalse(resource_front_end_dummy!!,"missingFood(pasta)")
		solveCheckGoal(resource!!,"stato(waitCmd, _, _)")
		
 	}
	
//----------------------------------------
	
	fun sendCmdMessage( actor : ActorBasic, name : String, time : Long ){
		actor.scope.launch{
			println("--- sendCmdMessage cmd(testSync,$name)")
  			MsgUtil.sendMsg("cmd","cmd(testSync,$name)",actor)
 		}
		delay(time) //give time to do the move
  	}

 	
	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( goal  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestFridge expected 'success' found $result")
	}
	
	fun solveCheckGoalFalse( actor : ActorBasic, goal : String ){
		actor.solve( goal  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="fail","%%%%%%% TestFridge expected 'fail' found $result")
	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
