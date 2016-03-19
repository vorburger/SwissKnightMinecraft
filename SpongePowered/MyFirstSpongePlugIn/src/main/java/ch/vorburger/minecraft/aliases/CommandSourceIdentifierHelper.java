package ch.vorburger.minecraft.aliases;

import org.spongepowered.api.command.CommandSource;

public interface CommandSourceIdentifierHelper {

	// TODO @see (move package, and merge with?) IdentifiableHelper

	String getID(CommandSource commandSource);

}
