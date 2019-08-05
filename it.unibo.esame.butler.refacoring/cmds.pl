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
		
%% TESTING OF ACTUAL BUTLER CMDS
comando( prepare, null ) :-
	assert( azione( movimento, pantry , null, null, null) ),
	assert( azione( spostaOggetto, pantry, robot, piatto, silverware ) ),
	assert( azione( movimento, fridge, null, null, null ) ), 
	assert( azione( aggiungiOggetto, robot, torta, cibo, null ) ),
	assert( azione( notificaFrigo, rimuovi, torta, cibo, false ) ), 
	assert( azione( aggiungiOggetto, robot, crema, cibo, null  ) ),
	assert( azione( notificaFrigo, rimuovi, crema, cibo, false ) ), 
	assert( azione( movimento, table, null, null, null  ) ), 
	assert( azione( spostaOggetto, robot, table, piatto, silverware ) ), 
	assert( azione( spostaOggetto, robot, table, torta, cibo ) ), 
	assert( azione( spostaOggetto, robot, table, crema, cibo ) ), 
	assert( azione( movimento, home, null, null, null  ) ).
	
comando( addFood, NomeCibo) :-
	assert( azione( notificaFrigo, conferma, NomeCibo, cibo, true ) ).	

comando( successAddFood, NomeCibo) :-
	assert( azione( check, 1, 2, 3, 4 ) ),
	assert( azione( movimento, fridge, null, null, null ) ),
	assert( azione( aggiungiOggetto, robot, NomeCibo, cibo, null ) ),
	assert( azione( notificaFrigo, rimuovi, NomeCibo, cibo, false ) ),
	assert( azione( movimento, table, null, null, null ) ),
	assert( azione( spostaOggetto, robot, table, NomeCibo, cibo ) ),
	assert( azione( movimento, home, null, null, null ) ).
	
comando( clear, null ) :-
	assert( azione( movimento, table , null, null, null) ),
	spostaTutto( table, robot, _ ),
	assert( azione( movimento, fridge , null, null, null) ),
	msgFrigoTutto( aggiungi, robot, cibo ), 
	rimuoviTutto( robot, cibo ),
	assert( azione( movimento, dishwasher , null, null, null) ),
	spostaTutto( robot, dishwasher, silverware ),
	assert( azione( movimento, home, null, null, null  ) ).

%% Comandi Interni %%

spostaTutto( I1, I2, Categ ) :-
	inventario( I1, L ),
	spostaTutto_( L, I1, I2, Categ ).

spostaTutto_( [], I1, I2, Categ ).
spostaTutto_( [( Nome, Categ )|T], I1, I2, Categ  ) :-
	assert( azione( sposta, I1, I2, Nome, Categ ) ), !,
	spostaTutto_( T, I1, I2, Categ ).
spostaTutto_( [_|T], I1, I2, Categ  ) :-
	spostaTutto_( T, I1, I2 ).

msgFrigoTutto( Azione, Inventario, Categ ) :-
	inventario( Inventario, Lista ),
	msgFrigoTutto_( Lista, Azione, Categ ).

msgFrigoTutto_( [], Azione, Categ ).
msgFrigoTutto_( [(Nome, Categ)|T], Azione, Categ ) :-
	assert( azione( notificaFrigo, Azione, Nome, Categ, false ) ), !,
	msgFrigoTutto_( T, Azione, Categ ).
msgFrigoTutto_( [_|T], Azione, Categ ) :-
	msgFrigoTutto_( T, Azione, Categ ).

rimuoviTutto( Inventario, Categ ) :-
	inventario( Inventario, Lista ),
	rimuoviTutto_( Lista, Categ ).

rimuoviTutto_( [], Categ ).
rimuoviTutto_( [(Nome, Categ)|T], Categ ) :-
	assert( azione( rimuovi, Nome, Categ, null, null ) ), !,
	rimuoviTutto _( T, Categ ).
rimuoviTutto_( [_|T], Categ ) :-
	rimuoviTutto _( T, Categ ).
	
	
	

		

	