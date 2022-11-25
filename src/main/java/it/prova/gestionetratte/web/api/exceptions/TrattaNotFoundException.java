package it.prova.gestionetratte.web.api.exceptions;

public class TrattaNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TrattaNotFoundException() {

	}

	public TrattaNotFoundException(String message) {
		super(message);
	}

}
