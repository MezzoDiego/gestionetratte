package it.prova.gestionetratte.web.api.exceptions;

public class RimozioneTrattaNonAnnullataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RimozioneTrattaNonAnnullataException() {

	}

	public RimozioneTrattaNonAnnullataException(String message) {
		super(message);
	}

}
