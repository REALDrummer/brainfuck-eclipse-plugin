package net.progsign.eclipse.plugin.brainfuck.runtime;

/**
 * Exceptions wrapper
 * @author Jacek Elczyk
 */
public class BFExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor
	 */
	public BFExecutionException() {
	}
	
	/**
	 * @param message
	 * @see RuntimeException#RuntimeException(String)
	 */
	public BFExecutionException(String message) {
		super(message);
	}
}
