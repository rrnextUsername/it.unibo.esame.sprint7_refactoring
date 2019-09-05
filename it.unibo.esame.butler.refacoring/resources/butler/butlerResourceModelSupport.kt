package itunibo.butler

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoapButler

object butlerResourceModelSupport{
	lateinit var resourcecoap : modelResourceCoapButler
	
	fun setCoapResource( rescoap : modelResourceCoapButler ){
		resourcecoap = rescoap
	}
	
	
	fun emitMissingFood( actor: ActorBasic, content: String ){
		println("			resourceModelSupport updateFridgeModel content=$content")
			actor.scope.launch{
				actor.emit( "missingFood" , "content( missingFood( state( $content )))" )
 			}	
	}
	
	fun updateRoomModel( actor: ActorBasic, inventory:String, content: String ){
		println("			resourceModelSupport updateRoomModel content=$content")
			actor.scope.launch{
				actor.emit( "modelContent" , "content( $inventory( state( $content )))" )
				resourcecoap.updateState("$inventory:: $content")
 			}	
	}
	
	
}

