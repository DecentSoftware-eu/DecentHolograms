package eu.decentsoftware.holograms.api.commands;

/**
 * This class represents an exception that may be thrown while executing a command.
 */
public class DecentCommandException extends Exception {

	public DecentCommandException(String message) {
		super(message);
	}

}
