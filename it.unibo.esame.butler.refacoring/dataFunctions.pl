aggiungi(Inventario, Nome, Categoria) :-
	retract( inventario( Inventario, Lista ) ),
	assert( inventario( Inventario, [( Nome, Categoria )|Lista] ) ).
	
rimuovi(Inventario, Nome, Categoria) :-
	retract(inventario(Inventario, ListaOld)),
	rimuovi_(ListaOld, ListaNew, (Nome, Categoria)),
	assert(inventario(Inventario, ListaNew)).
	
sposta(InventarioVecchio, InventarioNuovo,  Nome, Categoria) :-
	rimuovi( InventarioVecchio, Nome, Categoria ),
	aggiungi( InventarioNuovo, Nome, Categoria ).
	
presenza(Inventario, Nome, Categoria) :-
	inventario(Inventario, Lista),
	presente(Lista, (Nome, Categoria)).
	
%% Comandi Interni %%

rimuovi_([], [], Oggetto). %% Solo se non deve esplodere quando togli un elemento non esistente
rimuovi_([Oggetto|T1], T1, Oggetto).
rimuovi_([H|T1], [H|T2], Oggetto) :-
	rimuovi_(T1, T2, Oggetto).

presente([Oggetto|_], Oggetto).
presente([H|T], Oggetto) :-
	presente(T, Oggetto).
