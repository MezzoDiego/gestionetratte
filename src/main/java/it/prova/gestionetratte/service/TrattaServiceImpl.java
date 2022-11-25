package it.prova.gestionetratte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.repository.tratta.TrattaRepository;

@Service
public class TrattaServiceImpl implements TrattaService{
	
	@Autowired
	private TrattaRepository repository;

	@Override
	public List<Tratta> listAllElements(boolean eager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tratta caricaSingoloElemento(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tratta caricaSingoloElementoEager(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tratta aggiorna(Tratta trattaInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tratta inserisciNuovo(Tratta trattaInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rimuovi(Long idToRemove) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tratta> findByExample(Tratta example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tratta> findByCodiceAndDescrizione(String codice, String descrizione) {
		// TODO Auto-generated method stub
		return null;
	}

}
