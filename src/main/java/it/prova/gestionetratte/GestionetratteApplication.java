package it.prova.gestionetratte;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.gestionetratte.model.Airbus;
import it.prova.gestionetratte.model.Tratta;
import it.prova.gestionetratte.service.AirbusService;
import it.prova.gestionetratte.service.TrattaService;

@SpringBootApplication
public class GestionetratteApplication implements CommandLineRunner {

	@Autowired
	private AirbusService airbusService;

	@Autowired
	private TrattaService trattaService;

	public static void main(String[] args) {
		SpringApplication.run(GestionetratteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Airbus airbusRT = airbusService.findByCodiceAndDescrizione("554468ab", "Roma-Tokyo");

		if (airbusRT == null) {
			airbusRT = new Airbus("554468ab", "Roma-Tokyo",
					LocalDate.parse("01-10-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")), (Integer) 100);
			airbusService.inserisciNuovo(airbusRT);
		}

		Tratta romaTokyo = new Tratta("123ab", "Roma-Tokyo",
				LocalDate.parse("01-10-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				LocalTime.parse("10.00", DateTimeFormatter.ofPattern("HH.mm")),
				LocalTime.parse("20.30", DateTimeFormatter.ofPattern("HH.mm")), airbusRT);
		if (trattaService.findByCodiceAndDescrizione(romaTokyo.getCodice(), romaTokyo.getDescrizione()).isEmpty())
			trattaService.inserisciNuovo(romaTokyo);

		// ----------------------

		Airbus airbusMN = airbusService.findByCodiceAndDescrizione("5986538bx", "Milano-Napoli");

		if (airbusMN == null) {
			airbusMN = new Airbus("5986538bx", "Milano-Napoli",
					LocalDate.parse("13-07-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")), (Integer) 50);
			airbusService.inserisciNuovo(airbusMN);
		}

		Tratta milanoNapoli = new Tratta("223bx", "Milano-Napoli",
				LocalDate.parse("13-07-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				LocalTime.parse("08.00", DateTimeFormatter.ofPattern("HH.mm")),
				LocalTime.parse("10.30", DateTimeFormatter.ofPattern("HH.mm")), airbusMN);
		if (trattaService.findByCodiceAndDescrizione(milanoNapoli.getCodice(), milanoNapoli.getDescrizione()).isEmpty())
			trattaService.inserisciNuovo(milanoNapoli);

	}

}
