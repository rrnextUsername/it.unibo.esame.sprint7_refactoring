package itunibo.robot

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoap

object resourceModelSupport{
lateinit var resourcecoap : modelResourceCoap
	
	fun setCoapResource( rescoap : modelResourceCoap ){
		resourcecoap = rescoap
	}
	
	fun updateRobotModel( actor: ActorBasic, content: String ){
 			actor.solve(  "action(robot, move($content))" ) //change the robot state model
			actor.solve(  "model( A, robot, STATE )" )
			val RobotState = actor.getCurSol("STATE")
			//println("			resourceModelSupport updateModel RobotState=$RobotState")
			actor.scope.launch{
 				actor.emit( "modelChanged" , "modelChanged(  robot,  $content)" )  //for the robotmind
				actor.emit( "modelContent" , "content( robot( $RobotState ))" )
				resourcecoap.updateState( "robot( $RobotState )" )
  			}	
	}	
	fun updateSonarRobotModel( actor: ActorBasic, content: String ){
 			actor.solve( "action( sonarRobot,  $content )" ) //change the robot state model
			actor.solve( "model( A, sonarRobot, STATE )" )
			val SonarState = actor.getCurSol("STATE")
			//println("			resourceModelSupport updateSonarRobotModel SonarState=$SonarState")
			actor.scope.launch{
 				actor.emit( "modelContent" , "content( sonarRobot( $SonarState ))" )
				resourcecoap.updateState( "sonarRobot( $SonarState )" )
 			}	
	}
	
	fun updateRoomMapModel( actor: ActorBasic, content: String ){
		println("			resourceModelSupport updateRoomMapModel content=$content")
			actor.scope.launch{
				actor.emit( "modelContent" , "content( roomMap( state( '$content' )))" )
 				resourcecoap.updateState( "roomMap( '$content' )" )
 			}	
	}
	
	fun updateFridgeModel( actor: ActorBasic, content: String ){
		println("			resourceModelSupport updateFridgeModel content=$content")
			actor.scope.launch{
				actor.emit( "modelContent" , "content( fridgeInv( state( $content )))" )
 				//resourcecoap.updateState( "fridge_inv( '$content' )" )
 			}	
	}
	
	fun updateMissingFoodModel( actor: ActorBasic, content: String ){
		println("			resourceModelSupport updateFridgeModel content=$content")
			actor.scope.launch{
				actor.emit( "missingFood" , "content( missingFood( state( $content )))" )
 				//resourcecoap.updateState( "fridge_inv( '$content' )" )
 			}	
	}
	
	fun updateRoomModel( actor: ActorBasic, inventory:String, content: String ){
		println("			resourceModelSupport updateRoomModel inventory=$inventory content=$content")
			actor.scope.launch{
				actor.emit( "modelContent" , "content( $inventory( state( $content )))" )
 				//resourcecoap.updateState( "$inventory( '$content' )" )
 			}	
	}
}

