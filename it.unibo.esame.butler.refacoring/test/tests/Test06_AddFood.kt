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

class TestAddFood {
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
		    println(" %%%%%%% TestFridge getActors resource=${fridge_solver} fridge_model_handler=${fridge_solver}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestFridge terminate ")
	}
 
	@Test
	fun addFoodTest() {
		println(" %%%%%%% TestFridge  addFoodTest ")
		sendMessageToFridge(fridge_solver!!,"panzerotto",3000)
		solveCheckGoal(fridge_solver!!,"presenza(frigoInv, panzerotto, cibo)")
 	}
//----------------------------------------
	
	fun sendMessageToFridge( actor : ActorBasic, name : String, time : Long ){
		actor.scope.launch{
			println("--- sendMessageToFridge msgFridge(aggiungi,$name,cibo)")
  			MsgUtil.sendMsg("msgFridge","msgFridge(aggiungi,$name,cibo)",actor)
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
