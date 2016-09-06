package ch.vorburger.learning;

public class LearningServiceException extends Exception {
	private static final long serialVersionUID = 5426151440815984742L;

	public LearningServiceException(String message) {
		super(message);
	}

	public LearningServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
