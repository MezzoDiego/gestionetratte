package it.prova.gestionetratte.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionetratte.dto.AirbusDTO;
import it.prova.gestionetratte.dto.TrattaDTO;
import it.prova.gestionetratte.model.Airbus;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.repository.airbus.AirbusRepository;
import it.prova.gestionetratte.repository.tratta.TrattaRepository;
import it.prova.gestionetratte.web.api.exceptions.AirbusConTratteAssociateException;
import it.prova.gestionetratte.web.api.exceptions.AirbusNotFoundException;

@Service
public class AirbusServiceImpl implements AirbusService {

	@Autowired
	private AirbusRepository repository;

	@Autowired
	private TrattaRepository trattaRepository;

	@Override
	public List<Airbus> listAllElements() {
		return (List<Airbus>) repository.findAll();
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

		if (airbusToBeRemoved == null)
			throw new AirbusNotFoundException("Airbus not found con id: " + idToRemove);

		if (airbusToBeRemoved.getTratte().size() > 0)
			throw new AirbusConTratteAssociateException(
					"Impossibile eliminare airbus: sono presenti tratte ad esso associate.");

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

	@Override
	public List<AirbusDTO> findListaAirbusEvidenziandoSovrapposizioni() {
		List<AirbusDTO> airbusConRelativeTratte = AirbusDTO.createAirbusDTOListFromModelList(repository.findAllEager(), true, false);
		for (AirbusDTO airbusItem : airbusConRelativeTratte) {
			for (TrattaDTO trattaItem : airbusItem.getTratte()) {
				for (TrattaDTO item : airbusItem.getTratte()) {
					if ((item.getOraDecollo().isAfter(trattaItem.getOraDecollo())
							&& item.getOraDecollo().isBefore(trattaItem.getOraAtterraggio()))
							|| (item.getOraAtterraggio().isAfter(trattaItem.getOraDecollo())
									&& item.getOraAtterraggio().isBefore(trattaItem.getOraAtterraggio()))) {
						airbusItem.setConSovrapposizioni(true);

					}
				}
			}
		}
		 airbusConRelativeTratte.stream().map(airbusEntity -> {
			airbusEntity.setTratte(null);
			return airbusEntity;
		}).collect(Collectors.toList());
		 
		 return airbusConRelativeTratte;

	}

}
