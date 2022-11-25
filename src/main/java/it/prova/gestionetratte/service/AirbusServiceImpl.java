package it.prova.gestionetratte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionetratte.model.Airbus;
import it.prova.gestionetratte.repository.airbus.AirbusRepository;
import it.prova.gestionetratte.web.api.exceptions.AirbusConTratteAssociateException;
import it.prova.gestionetratte.web.api.exceptions.AirbusNotFoundException;

@Service
public class AirbusServiceImpl implements AirbusService {

	@Autowired
	private AirbusRepository repository;

	@Override
	public List<Airbus> listAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Airbus> listAllElementsEager() {
		return repository.findAllEager();
	}

	@Override
	public Airbus caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Airbus caricaSingoloElementoConTratte(Long id) {
		return repository.findByIdEager(id);
	}

	@Override
	@Transactional
	public Airbus aggiorna(Airbus airbusInstance) {
		return repository.save(airbusInstance);
	}

	@Override
	@Transactional
	public Airbus inserisciNuovo(Airbus airbusInstance) {
		return repository.save(airbusInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		Airbus airbusToBeRemoved = repository.findByIdEager(idToRemove);
		
		if(airbusToBeRemoved == null) 
			throw new AirbusNotFoundException("Airbus not found con id: " + idToRemove);
		
		if(airbusToBeRemoved.getTratte().size() > 0)
			throw new AirbusConTratteAssociateException("Impossibile eliminare airbus: sono presenti tratte ad esso associate.");
		
		repository.deleteById(idToRemove);

	}

	@Override
	public List<Airbus> findByExample(Airbus example) {
		return repository.findByExample(example);
	}

	@Override
	public Airbus findByCodiceAndDescrizione(String codice, String descrizione) {
		return repository.findByCodiceAndDescrizione(codice, descrizione);
	}

}
