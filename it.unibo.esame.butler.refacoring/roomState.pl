inventario( tableInv , [(coltello,silverware),(cannelloni,cibo),(pizza,cibo)] ).
inventario( butlerInv , [(panzerotto,cibo),(cassata,cibo),(tazza,silverware)] ).
inventario( pantryInv , [(piatto,silverware)] ).
inventario( dishwasherInv , [] ).

showRoomState :- 
	output(" ---------- ROOM CURRENT STATE ---------- "),
	showResources,
	output(" ------------------------------------------ ").
		
showResources :- 
	inventario( tableInv , T ),
	outputL("|"),
 	output( inventario( tableInv , T ) ),
	inventario( butlerInv , B ),
	outputL("|"),
 	output( inventario( butlerInv , B ) ),
	inventario( pantryInv , P ),
	outputL("|"),
 	output( inventario( pantryInv , P ) ),
	inventario( dishwasherInv , D ),
	outputL("|"),
 	output( inventario( dishwasherInv , D ) ),
	fail.
showResources.			

output( M ) :- stdout <- println( M ).
outputL( M ) :- stdout <- print( M ).

initResourceTheory :- output("resourceModel loaded").
:- initialization(initResourceTheory).