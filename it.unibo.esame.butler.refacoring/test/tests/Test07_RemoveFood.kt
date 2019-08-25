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
  
class TestRemoveFood {
  var fridge_solver : ActorBasic? = null
	
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
			fridge_solver = sysUtil.getActor("fridge")	
		    println(" %%%%%%% TestFridge getActors fridge_solver=${fridge_solver} fridge_model_handler=${fridge_solver}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestFridge terminate ")
	}
 
	@Test
	fun removeFoodTest() {
		println(" %%%%%%% TestFridge  removeFoodTest ")
		sendMessageToFridge(fridge_solver!!,"torta",1000)
		solveCheckGoalFalse(fridge_solver!!,"presenza(frigoInv, torta, cibo)")
 	}
//----------------------------------------
	
	fun sendMessageToFridge( actor : ActorBasic, name : String, time : Long ){
			println("--- sendMessageToFridge msgFridge(rimuovi,$name,cibo)")
		actor.scope.launch{
  			MsgUtil.sendMsg("msgFridge","msgFridge(rimuovi,$name,cibo)",actor)
 		}
		delay(time) //give time to do the move
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
