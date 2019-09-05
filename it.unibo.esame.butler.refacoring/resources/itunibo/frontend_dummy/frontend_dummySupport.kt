package itunibo.frontend_dummy

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import it.unibo.kactor.ApplMessage

object frontend_dummySupport{
	
	fun persist( actor: ActorBasic, currentMsg: ApplMessage){
			actor.scope.launch{
				
				val fridge_inventory= currentMsg.msgContent()				
				actor.solve("assert(result( frigo , $fridge_inventory ))")
  			}	
	}	
}

