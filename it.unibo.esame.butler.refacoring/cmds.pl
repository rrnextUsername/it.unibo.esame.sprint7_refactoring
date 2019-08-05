comando( testMain, null ) :-
	assert( azione( check, 1, 1, 1, 1 ) ),
	assert( azione( wait, 1, 1, 1, 1 ) ),
	assert( azione( check, 2, 2, 2, 2 ) ).
	
comando( testModificaInventario, null ) :-
	assert( azione( notificaFrigo, aggiungi, budino, cibo, false ) ),
	assert( azione( notificaFrigo, rimuovi, torta, cibo, false ) ),
	assert( azione( aggiungiOggetto, robot, piatto, silverware, null ) ),
	assert( azione( spostaOggetto, robot, dishwasher, tazza, silverware ) ),
	assert( azione( rimuoviOggetto, table, pizza, cibo, null ) ),
	assert( azione( notificaFrigo, conferma, torta, cibo, true ) ),
	assert( azione( notificaFrigo, conferma, budino, cibo, true ) ).
	
comando( testSync, NomeCibo ) :-
	assert( azione( notificaFrigo, conferma, NomeCibo, cibo, true ) ).

comando( successAddFood, NomeCibo) :-
	assert( azione( check, 1, 2, 3, 4 ) ).
	
comando( testASync, null ):-
	assert( azione( notificaFrigo, null, null, null, false ) ).
	
comando( testDestination, null ):-
	assert( azione( movimento, location1, null, null, null ) ).
	
comando( testOrder, null ):-
	assert( azione( movimento, location1, null, null, null ) ),
	assert( azione( check, check1, null, null, null ) ),
	assert( azione( movimento, location2, null, null, null ) ),
	assert( azione( check, check2, null, null, null ) ),
	assert( azione( movimento, home, null, null, null ) ).
	
comando( goHome, null):-
	assert( azione( movimento, home, null, null, null ) ).
	
	
	