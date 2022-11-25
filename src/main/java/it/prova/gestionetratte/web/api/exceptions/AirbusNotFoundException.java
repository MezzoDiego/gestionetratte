package it.prova.gestionetratte.web.api.exceptions;

public class AirbusNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AirbusNotFoundException() {
		
	}
	
	public AirbusNotFoundException(String message) {
		super(message);
	}
	
}
