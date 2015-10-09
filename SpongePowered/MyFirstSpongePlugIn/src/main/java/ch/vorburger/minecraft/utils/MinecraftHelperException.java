package ch.vorburger.minecraft.utils;

/**
 * Michael's Minecraft Exception.
 * 
 * Just a checked Exception possibly thrown by some of the helper utility classes.
 * 
 * @author Michael Vorburger
 */
public class MinecraftHelperException extends Exception {
    private static final long serialVersionUID = 1;

	public MinecraftHelperException(String message) {
		super(message);
	}

	public MinecraftHelperException(String message, Throwable cause) {
		super(message, cause);
	}

}
