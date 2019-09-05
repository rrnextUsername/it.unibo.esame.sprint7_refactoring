package itunibo.coap.client

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import itunibo.coap.observer.AsynchListener
import it.unibo.kactor.ActorBasic

object coapClientButler{
	lateinit var actor : ActorBasic
	private lateinit var coapClient: CoapClient
		
	fun create( a: ActorBasic, serverAddr: String, port: Int, resourceName: String?  ){
			actor = a
			coapClient = CoapClient("coap://$serverAddr:" + port + "/" + resourceName)
			println("--------------------------------------------------")
			println("Coap Client [$a:: coap://$serverAddr:$port/$resourceName] started");	
			println("--------------------------------------------------")
 	}		
	
	
	fun createClient(serverAddr: String, port: Int, resourceName: String?) {
		coapClient = CoapClient("coap://$serverAddr:" + port + "/" + resourceName)
		println("Client started")
	}

	fun synchGet() { //Synchronously send the GET message (blocking call)
		println("%%% synchGet ")
//		CoapResponse coapResp = coapClient.advanced(request);
		val coapResp = coapClient.get()
//The "CoapResponse" message contains the response.
 		//println(Utils.prettyPrint(coapResp))
		println(coapResp.responseText)
	}

	fun put(v: String) {
		synchGet()
		println("put $v")
		val coapResp = coapClient.put(v, MediaTypeRegistry.TEXT_PLAIN)
//The "CoapResponse" message contains the response.
		println("%%% ANSWER put $v:")
		//println(Utils.prettyPrint(coapResp))
		println(coapResp.responseText)
	}

 	fun asynchGet() {
 		coapClient.get( AsynchListener );
	}
}

