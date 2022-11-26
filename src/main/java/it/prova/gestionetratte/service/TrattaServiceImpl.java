package it.prova.gestionetratte.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionetratte.model.Stato;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.repository.tratta.TrattaRepository;
import it.prova.gestionetratte.web.api.exceptions.RimozioneTrattaNonAnnullataException;
import it.prova.gestionetratte.web.api.exceptions.TrattaNotFoundException;
import it.prova.gestionetratte.web.api.exceptions.TratteAttiveNotFoundException;

@Service
public class TrattaServiceImpl implements TrattaService {

	@Autowired
	private TrattaRepository repository;

	@Override
	public List<Tratta> listAllElements(boolean eager) {
		if (eager)
			return repository.findAllTrattaEager();

		return (List<Tratta>) repository.findAll();
	}

	@Override
	public Tratta caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Tratta caricaSingoloElementoEager(Long id) {
		return repository.findSingleTrattaEager(id);
	}

	@Override
	@Transactional
	public Tratta aggiorna(Tratta trattaInstance) {
		return repository.save(trattaInstance);
	}

	@Override
	@Transactional
	public Tratta inserisciNuovo(Tratta trattaInstance) {
		return repository.save(trattaInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		Tratta trattaToBeRemoved = repository.findById(idToRemove)
				.orElseThrow(() -> new TrattaNotFoundException("Tratta not found con id: " + idToRemove));

		if (trattaToBeRemoved.getStato() == Stato.ATTIVA || trattaToBeRemoved.getStato() == Stato.CONCLUSA)
			throw new RimozioneTrattaNonAnnullataException(
					"Impossibile rimuovere tratta: deve essere ANNULLATA per poter essere eliminata.");

		repository.deleteById(idToRemove);

	}

	@Override
	public List<Tratta> findByExample(Tratta example) {
		return repository.findByExample(example);
	}

	@Override
	public List<Tratta> findByCodiceAndDescrizione(String codice, String descrizione) {
		return repository.findByCodiceAndDescrizione(codice, descrizione);
	}

	@Override
	@Transactional
	public void concludiTratte() {
		List<Tratta> tratteAttive = repository.findAllByStatoLike(Stato.ATTIVA);

		if (tratteAttive.size() < 1)
			throw new TratteAttiveNotFoundException("Errore. non sono presenti tratte attive.");

		tratteAttive.stream().filter(trattaItem -> LocalTime.now().isAfter(trattaItem.getOraAtterraggio()))
				.forEach(trattaItem -> trattaItem.setStato(Stato.CONCLUSA));
		tratteAttive.forEach(trattaItem -> repository.save(trattaItem));
	}

}
