stato( null , null , null ).

aggiornaStato( Stato, CmdCorrente, CmdArg ) :-
	retract( stato( A , B , C ) ),
	assert( stato( Stato, CmdCorrente, CmdArg ) ),
	showSolverState.
	
aggiornaStato( Stato ) :-
	retract( stato( A , B , C ) ),
	assert( stato( Stato, B, C ) ),
	showSolverState.
	
showActions :-
	output("Azioni:"),
	showAzione.
	
showAzione :-
	azione( ARG1, ARG2, ARG3, ARG4, ARG5 ),
	output( azione( ARG1, ARG2, ARG3, ARG4, ARG5 ) ),
	fail.
showAzione.

showSolverState :- 
	output(" ---------- SOLVER CURRENT STATE ---------- "),
	showSolverResources,
	output(" ------------------------------------------ ").
		
showSolverResources :- 	 
	stato( STATO, CMD , ARG ),
	outputL("|"),
 	output( stato( STATO, CMD , ARG ) ),
 	azione( ARG1, ARG2, ARG3, ARG4, ARG5 ),
 	outputL("|--->"),
	output( azione( ARG1, ARG2, ARG3, ARG4, ARG5 ) ),
	fail.
showSolverResources.	

retractAllActions :-
	retract( azione( _, _, _, _, _ ) ),
	fail.
retractAllActions.



%%output( M ) :- stdout <- println( M ).
%%outputL( M ) :- stdout <- print( M ).

initResourceTheory :- output("solverResourceModel loaded").
:- initialization(initResourceTheory).