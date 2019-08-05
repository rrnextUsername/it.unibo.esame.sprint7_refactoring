inventario( frigo , [(torta,cibo),(panuozzi,cibo),(pasta,cibo)] ).

showResourceModel :- 
	output(" ---------- FRIDGE CURRENT STATE ---------- "),
	showResources,
	output(" ------------------------------------------ ").
		
showResources :- 
	inventario( F , C ),
	outputL("|"),
 	output( inventario( F , C ) ),
	fail.
showResources.			

output( M ) :- stdout <- println( M ).
outputL( M ) :- stdout <- print( M ).

initResourceTheory :- output("resourceModel loaded").
:- initialization(initResourceTheory).