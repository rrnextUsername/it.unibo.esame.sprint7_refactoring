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

class TestModificaInventario {
	var butler_solver: ActorBasic? = null
	var butler_fridge_handler: ActorBasic? = null
	var fridge_cmd_solver: ActorBasic? = null
	var frontend_dummy: ActorBasic? =null

	@BeforeEach
	fun systemSetUp() {
		GlobalScope.launch {
			println(" %%%%%%% TestButtler starts buttler mind ")
			it.unibo.ctxButler.main()
		}
		delay(10000)        //give the time to start
		butler_solver = sysUtil.getActor("butler_solver")
		butler_fridge_handler = sysUtil.getActor("butler_fridge_handler")
		fridge_cmd_solver = sysUtil.getActor("fridge")
		frontend_dummy = sysUtil.getActor("frontend_dummy")
		println(" %%%%%%% TestButtler getActors butler_solver=${butler_solver}, fridge_cmd_solver=${fridge_cmd_solver}")
	}

	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestButtler terminate ")
	}

	@Test
	fun addSolveCmd() {
		println(" %%%%%%% TestButtler  solveCmdTest ")
		sendCmdMessage(butler_solver!!, 10000)

		//completeded all tasks
		solveCheckGoal(butler_solver!!, "done( handleAdd, butlerInv, piatto, silverware )")
		solveCheckGoal(butler_solver!!, "done( handleSwap, butlerInv, dishwasherInv, tazza, silverware )")
		solveCheckGoal(butler_solver!!, "done( handleRemove, tableInv, pizza, cibo )")
		
		solveCheckGoal(butler_fridge_handler!!, "done( actionMsgFridgeSync, aggiungi, budino, cibo )")
		solveCheckGoal(butler_fridge_handler!!, "done( actionMsgFridgeSync, rimuovi, torta, cibo )")

		//presence or absence of objects
		checkObj(butler_solver!!, true, "presenza( butlerInv, piatto, silverware )")
		checkObj(butler_solver!!, false, "presenza( butlerInv, tazza, silverware )")
		checkObj(butler_solver!!, true, "presenza( dishwasherInv, tazza, silverware )")
		checkObj(butler_solver!!, false, "presenza( tableInv, pizza, silverware )")
		checkObj(fridge_cmd_solver!!, false, "presenza( frigoInv, torta, cibo )")
		checkObj(fridge_cmd_solver!!, true, "presenza( frigoInv, budino, cibo )")
		
		//messages received
		solveCheckGoal(fridge_cmd_solver!!, "received( aggiungi, budino, cibo )")
		solveCheckGoal(fridge_cmd_solver!!, "received( rimuovi, torta, cibo )")
		solveCheckGoal(fridge_cmd_solver!!, "received( conferma, budino, cibo )")
		solveCheckGoal(fridge_cmd_solver!!, "received( conferma, torta, cibo )")
		solveCheckGoal(fridge_cmd_solver!!, "received( conferma, pasta, cibo )", "fail")
		solveCheckGoal(frontend_dummy!!, "missingFood")
		
		

		//returned to waitCmd
		solveCheckGoal(butler_solver!!, "stato( waitCmd, _, _ )")
	}

//----------------------------------------

	fun solveCheckGoal(actor: ActorBasic, goal: String, expectedResult: String ="success") {
		actor.solve("retract($goal)")
		var result = actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result == expectedResult, "%%%%%%% TestButtler expected '$expectedResult' found $result")
	}

	fun sendCmdMessage(actor: ActorBasic, time: Long) {
		println("--- sendQueryMessage comando( testModificaInventario, null )")
		actor.scope.launch {
			MsgUtil.sendMsg("cmd", "cmd( testModificaInventario, null )", actor)
		}
		delay(time) //give time to do the move
	}

	fun delay(time: Long) {
		Thread.sleep(time)
	}

	fun checkObj(actor: ActorBasic, presence: Boolean, goal: String) {
		actor.solve(goal)
		var result = actor.resVar
		//if (presence) {val expected="yes" } else{ val expected="no"}
		println(" %%%%%%%  actor={$actor.name} goal= $goal expexted=${if (presence) "yes" else "no"} result = $result")
		if (presence)
			assertTrue(result == "success", "%%%%%%% TestButtler expected 'success' found $result")
		else
			assertTrue(result == "fail", "%%%%%%% TestButtler expected 'fail' found $result")
	}


}
