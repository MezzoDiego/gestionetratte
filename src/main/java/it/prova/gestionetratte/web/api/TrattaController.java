package it.prova.gestionetratte.web.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.gestionetratte.dto.TrattaDTO;
import it.prova.gestionetratte.model.Airbus;
import it.prova.gestionetratte.model.Stato;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.service.AirbusService;
import it.prova.gestionetratte.service.TrattaService;
import it.prova.gestionetratte.web.api.exceptions.CustomValidationException;
import it.prova.gestionetratte.web.api.exceptions.IdNotNullForInsertException;
import it.prova.gestionetratte.web.api.exceptions.TrattaNotFoundException;
import it.prova.gestionetratte.web.api.exceptions.TratteAttiveNotFoundException;

@RestController
@RequestMapping("api/tratta")
public class TrattaController {

	@Autowired
	private TrattaService trattaService;
	
	@Autowired
	private AirbusService airbusService;

	@GetMapping
	public List<TrattaDTO> getAll() {
		return TrattaDTO.createTrattaDTOListFromModelList(trattaService.listAllElements(true), true);
	}

	@GetMapping("/{id}")
	public TrattaDTO findById(@PathVariable(value = "id", required = true) long id) {
		Tratta tratta = trattaService.caricaSingoloElementoEager(id);

		if (tratta == null)
			throw new TrattaNotFoundException("Tratta not found con id: " + id);

		return TrattaDTO.buildTrattaDTOFromModel(tratta, true);
	}

	// gli errori di validazione vengono mostrati con 400 Bad Request ma
	// elencandoli grazie al ControllerAdvice
	@PostMapping
	public TrattaDTO createNew(@Valid @RequestBody TrattaDTO trattaInput) {
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (trattaInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		
		Airbus airbusPerValidazioni = airbusService.caricaSingoloElemento(trattaInput.getAirbus().getId());
		
		if(trattaInput.getData().isBefore(airbusPerValidazioni.getDataInizioServizio()))
			throw new CustomValidationException("Non e' possibile inserire una tratta avente la data di partenza inferiore a quella di inizio servizio dell airbus.");

		if((trattaInput.getStato() != null && trattaInput.getData().isAfter(LocalDate.now())) || (trattaInput.getStato() != null && trattaInput.getData().isEqual(LocalDate.now()) && trattaInput.getOraDecollo().isAfter(LocalTime.now())))
			throw new CustomValidationException("Non e' possibile inserire uno stato se la tratta deve essere ancora percorsa.");
		
		if(trattaInput.getStato() != null && trattaInput.getStato() == Stato.ATTIVA && trattaInput.getOraAtterraggio().isBefore(LocalTime.now()) && !trattaInput.getData().isAfter(LocalDate.now()))
			throw new CustomValidationException("E' possibile inserire una tratta attiva solo se l'airbus non e' gia' atterrato");
		
		if(trattaInput.getOraDecollo().isAfter(trattaInput.getOraAtterraggio()))
			throw new CustomValidationException("Non e' possibile inserire una tratta con orario di decollo maggiore di quello di atterraggio.");
		
		Tratta trattaInserita = trattaService.inserisciNuovo(trattaInput.buildTrattaModel());
		return TrattaDTO.buildTrattaDTOFromModel(trattaInserita, true);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		trattaService.rimuovi(id);
	}

	@PutMapping("/{id}")
	public TrattaDTO update(@Valid @RequestBody TrattaDTO trattaInput, @PathVariable(required = true) Long id) {
		Tratta tratta = trattaService.caricaSingoloElemento(id);
		Tratta trattaConAirbus = trattaService.caricaSingoloElementoEager(id);

		if (tratta == null)
			throw new TrattaNotFoundException("Tratta not found con id: " + id);
		
		if(trattaInput.getData().isBefore(trattaConAirbus.getAirbus().getDataInizioServizio()))
			throw new CustomValidationException("Non e' possibile modificare la data mettendola minore di quella di inizio servizio del relativo airbus.");
		
		if(trattaInput.getOraDecollo().isAfter(trattaInput.getOraAtterraggio()))
			throw new CustomValidationException("Non e' possibile modificare gli orari se l'orario di decollo succede quello di atterraggio.");
		
		if((trattaInput.getStato() != null && trattaInput.getStato() != Stato.ANNULLATA && trattaInput.getData().isAfter(LocalDate.now())) || (trattaInput.getStato() != null && trattaInput.getStato() != Stato.ANNULLATA && trattaInput.getData().isEqual(LocalDate.now()) && trattaInput.getOraDecollo().isAfter(LocalTime.now())))
			throw new CustomValidationException("Non e' possibile attivare o concludere una tratta se l'airbus deve ancora percorrerla.");
		
		if((trattaInput.getStato() != null && trattaInput.getStato() == Stato.ANNULLATA && trattaInput.getData().isBefore(LocalDate.now())) || (trattaInput.getStato() != null && trattaInput.getStato() == Stato.ANNULLATA && trattaInput.getData().isEqual(LocalDate.now()) && trattaInput.getOraAtterraggio().isBefore(LocalTime.now())))
			throw new CustomValidationException("Non e' possibile annullare una tratta se e' gia' stata percorsa.");
		
		if((trattaInput.getStato() != null && trattaInput.getStato() != Stato.ATTIVA && trattaInput.getData().isBefore(LocalDate.now())) || (trattaInput.getStato() != null && trattaInput.getStato() != Stato.ATTIVA && trattaInput.getData().isEqual(LocalDate.now()) && trattaInput.getOraAtterraggio().isAfter(LocalTime.now())))
			throw new CustomValidationException("Non e' possibile modificare lo stato di una tratta se la sta attualmente percorrendo l'airbus");
		

		trattaInput.setId(id);
		Tratta trattaAggiornata = trattaService.aggiorna(trattaInput.buildTrattaModel());
		return TrattaDTO.buildTrattaDTOFromModel(trattaAggiornata, false);
	}
	
	@PostMapping("/search")
	public List<TrattaDTO> search(@RequestBody TrattaDTO example) {
		return TrattaDTO.createTrattaDTOListFromModelList(trattaService.findByExample(example.buildTrattaModel()),
				false);
	}
	
	@GetMapping("/concludiTratte")
	public void concludiTratte() {
		trattaService.concludiTratte();
	}

}
