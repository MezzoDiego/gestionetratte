package it.prova.gestionetratte.service;

import java.util.List;

import it.prova.gestionetratte.model.Tratta;

public interface TrattaService {
	List<Tratta> listAllElements(boolean eager);

	Tratta caricaSingoloElemento(Long id);

	Tratta caricaSingoloElementoEager(Long id);

	Tratta aggiorna(Tratta trattaInstance);

	Tratta inserisciNuovo(Tratta trattaInstance);

	void rimuovi(Long idToRemove);

	List<Tratta> findByExample(Tratta example);

	List<Tratta> findByCodiceAndDescrizione(String codice, String descrizione);
}
