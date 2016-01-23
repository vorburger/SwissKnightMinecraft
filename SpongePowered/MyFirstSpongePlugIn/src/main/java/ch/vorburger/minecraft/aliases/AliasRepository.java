package ch.vorburger.minecraft.aliases;

import java.io.IOException;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;

public interface AliasRepository {

	// TODO CommandSource or Player?

	void createAlias(Player player, AliasDescription description, String definition) throws IOException;

	String getAlias(Player player, String aliasName) throws IllegalArgumentException, IOException;

	void deleteAlias(Player player, String aliasName) throws IllegalArgumentException, IOException;

	List<AliasDescription> getAliases(Player player) throws IOException;

	interface AliasDescription {
		String getName();
		// String getDescription();
	}

	class AliasDescriptionImpl implements AliasDescription {

		private final String name;
		//		private final String description;

		public AliasDescriptionImpl(String name /*, String description*/) {
			this.name = name;
			//	this.description = description;
		}

		@Override
		public String getName() {
			return name;
		}

		//		@Override
		//		public String getDescription() {
		//			return description;
		//		}

	}
}
