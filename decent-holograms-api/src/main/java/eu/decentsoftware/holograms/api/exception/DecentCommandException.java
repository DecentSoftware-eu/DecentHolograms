package eu.decentsoftware.holograms.api.exception;

import eu.decentsoftware.holograms.utils.Common;

/**
 * This class represents an exception that may be thrown while executing a command.
 */
public class DecentCommandException extends Exception {

	public DecentCommandException(String message) {
		super(message.replace("{prefix}", Common.PREFIX));
	}

}
