package itunibo.fridge

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoap

object fridgeResourceModelSupport{
lateinit var resourcecoap : modelResourceCoap
	
	fun setCoapResource( rescoap : modelResourceCoap ){
		resourcecoap = rescoap
	}
	
	fun exposeFridgeModel( actor: ActorBasic){
			actor.scope.launch{
				actor.solve("inventario( frigo , INVENTORY )")
				
				var result = actor.getCurSol("INVENTORY")
				actor.emit( "modelContent" , "content( frigo, $result  )" )
				//resourcecoap.updateState( "fridge( $resVar )" )
  			}	
	}	
}

