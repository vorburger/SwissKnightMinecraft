package ch.vorburger.minecraft.aliases;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.entity.living.player.Player;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FilesAliasRepository extends InMemoryAliasRepository {

	protected File root;

	public FilesAliasRepository(CommandSourceIdentifierHelper h, File rootDirectory) {
		super(h);
		this.root = rootDirectory;
		// TODO load stored aliases found under rootDirectory into memory, calling super.createAlias
	}

	@Override
	public void createAlias(Player player, AliasDescription description, String definition) throws IOException {
		super.createAlias(player, description, definition);
		File aliasFile = getAliasFile(player, description.getName());
		aliasFile.delete();
		Files.append(definition, aliasFile, Charsets.UTF_8);
	}

	@Override
	public void deleteAlias(Player player, String aliasName) throws IOException {
		super.deleteAlias(player, aliasName);
		File aliasFile = getAliasFile(player, aliasName);
		aliasFile.delete();
	}

	protected File getAliasFile(Player player, String aliasName) throws IOException {
		String id = h.getID(player);
		File playerDir = new File(root, id);
		File aliasFile = new File(playerDir, aliasName);
		return aliasFile;
	}

}
