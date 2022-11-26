package it.prova.gestionetratte.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.gestionetratte.model.Airbus;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirbusDTO {

	private Long id;

	@NotBlank(message = "{codice.notblank}")
	private String codice;

	@NotBlank(message = "{descrizione.notblank}")
	@Size(min = 5, max = 50, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String descrizione;

	@NotNull(message = "{dataInizioServizio.notnull}")
	private LocalDate dataInizioServizio;

	@NotNull(message = "{numeroPasseggeri.notnull}")
	private Integer numeroPasseggeri;
	
	private Boolean conSovrapposizioni;

	@JsonIgnoreProperties(value = { "airbus" })
	private Set<TrattaDTO> tratte = new HashSet<TrattaDTO>(0);

	public AirbusDTO() {
		super();
	}

	public AirbusDTO(Long id) {
		super();
		this.id = id;
	}

	public AirbusDTO(Long id, String codice, String descrizione, LocalDate dataInizioServizio,
			Integer numeroPasseggeri) {
		super();
		this.id = id;
		this.codice = codice;
		this.descrizione = descrizione;
		this.dataInizioServizio = dataInizioServizio;
		this.numeroPasseggeri = numeroPasseggeri;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDate getDataInizioServizio() {
		return dataInizioServizio;
	}

	public void setDataInizioServizio(LocalDate dataInizioServizio) {
		this.dataInizioServizio = dataInizioServizio;
	}

	public Integer getNumeroPasseggeri() {
		return numeroPasseggeri;
	}

	public void setNumeroPasseggeri(Integer numeroPasseggeri) {
		this.numeroPasseggeri = numeroPasseggeri;
	}
	
	public Boolean getConSovrapposizioni() {
		return conSovrapposizioni;
	}

	public void setConSovrapposizioni(Boolean conSovrapposizioni) {
		this.conSovrapposizioni = conSovrapposizioni;
	}

	public Set<TrattaDTO> getTratte() {
		return tratte;
	}

	public void setTratte(Set<TrattaDTO> tratte) {
		this.tratte = tratte;
	}
	
	public Airbus buildAirbusModel() {
		return new Airbus(this.id, this.codice, this.descrizione, this.dataInizioServizio, this.numeroPasseggeri);
	}
	
	public static AirbusDTO buildAirbusDTOFromModel(Airbus airbusModel, boolean includeTratte, boolean includeSovrapposizioni) {
		AirbusDTO result = new AirbusDTO(airbusModel.getId(), airbusModel.getCodice(), airbusModel.getDescrizione(),
				airbusModel.getDataInizioServizio(), airbusModel.getNumeroPasseggeri());
		if(includeTratte)
			result.setTratte(TrattaDTO.createTrattaDTOSetFromModelSet(airbusModel.getTratte(), false));
		
		if(includeSovrapposizioni)
			result.setConSovrapposizioni(true);
		return result;
	}
	
	public static List<AirbusDTO> createAirbusDTOListFromModelList(List<Airbus> modelListInput, boolean includeTratte, boolean includeSovrapposizioni) {
		return modelListInput.stream().map(airbusEntity -> {
			AirbusDTO result = AirbusDTO.buildAirbusDTOFromModel(airbusEntity,includeTratte, includeSovrapposizioni);
			if(includeTratte)
				result.setTratte(TrattaDTO.createTrattaDTOSetFromModelSet(airbusEntity.getTratte(), false));
			if(includeSovrapposizioni)
				result.setConSovrapposizioni(true);
			return result;
		}).collect(Collectors.toList());
	}

}
