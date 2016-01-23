package ch.vorburger.minecraft.aliases;

import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.command.CommandSource;

public class CommandSourceIdentifierHelperImpl implements CommandSourceIdentifierHelper {

	// TODO @see (move package, and merge with?) IdentifiableHelper

	public CommandSourceIdentifierHelperImpl() {
	}

	@Override
	public String getID(CommandSource commandSource) {
		if (commandSource instanceof Identifiable) {
			Identifiable identifiable = (Identifiable) commandSource;
			return identifiable.getUniqueId().toString();
		} else {
			throw new IllegalArgumentException("Not (yet?) implemented: " + commandSource.toString());
		}
	}

}
