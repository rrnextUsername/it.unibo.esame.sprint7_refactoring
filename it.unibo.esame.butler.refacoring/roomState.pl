inventario( table , [(pizza,cibo)] ).
inventario( robot , [(panzerotto,cibo),(tazza,silverware)] ).
inventario( pantry , [] ).
inventario( dishwasher , [] ).

showRoomState :- 
	output(" ---------- FRIDGE CURRENT STATE ---------- "),
	showResources,
	output(" ------------------------------------------ ").
		
showResources :- 
	inventario( table , T ),
	outputL("|"),
 	output( inventario( table , T ) ),
	inventario( robot , R ),
	outputL("|"),
 	output( inventario( robot , R ) ),
	inventario( pantry , P ),
	outputL("|"),
 	output( inventario( pantry , P ) ),
	inventario( dishwasher , D ),
	outputL("|"),
 	output( inventario( dishwasher , D ) ),
	fail.
showResources.			

output( M ) :- stdout <- println( M ).
outputL( M ) :- stdout <- print( M ).

initResourceTheory :- output("resourceModel loaded").
:- initialization(initResourceTheory).