package it.prova.gestionetratte.web.api.exceptions;

public class CustomValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomValidationException() {

	}

	public CustomValidationException(String message) {
		super(message);
	}

}
