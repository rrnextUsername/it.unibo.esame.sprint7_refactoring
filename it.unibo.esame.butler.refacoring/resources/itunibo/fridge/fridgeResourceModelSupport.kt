package itunibo.fridge

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoapFridge

object fridgeResourceModelSupport{
lateinit var resourcecoap : modelResourceCoapFridge
	
	fun setCoapResource( rescoap : modelResourceCoapFridge ){
		resourcecoap = rescoap
	}
	
	fun updateFridgeModel( actor: ActorBasic, content: String ){
		println("			resourceModelSupport updateFridgeModel content=$content")
			actor.scope.launch{
				actor.emit( "modelContent" , "content( fridgeInv( state( $content )))" )
				resourcecoap.updateState(content)
 			}	
	}
}

