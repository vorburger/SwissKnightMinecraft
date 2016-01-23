package ch.vorburger.minecraft.aliases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.entity.living.player.Player;

public class InMemoryAliasRepository implements AliasRepository {

	protected final CommandSourceIdentifierHelper h;
	private Map<String, Map<String, String>> map = new HashMap<>(); // TODO Find some concurrency safe yet fast Map impl to use here

	public InMemoryAliasRepository(CommandSourceIdentifierHelper h) {
		super();
		this.h = h;
	}

	@Override
	public void createAlias(Player player, AliasDescription description, String definition) throws IOException {
		String id = h.getID(player);
		Map<String, String> innerMap = map.get(id);
		if (innerMap == null) {
			innerMap = new HashMap<>(); // regular HashMap (non synchronized, not concurrency safe) should be fine here; it's unlikely a Player creates two Aliases concurrently
			map.put(id, innerMap);
		}
		innerMap.put(description.getName(), definition);
	}

	@Override
	public String getAlias(Player player, String aliasName) throws IllegalArgumentException {
		Map<String, String> innerMap = getInnerMap(player);
		String alias = innerMap.get(aliasName);
		if (alias == null)
			throw new IllegalArgumentException("Unknonwn alias (for this player): " + aliasName);
		return alias;
	}

	protected Map<String, String> getInnerMap(Player player) {
		String id = h.getID(player);
		Map<String, String> innerMap = map.get(id);
		if (innerMap == null)
			throw new IllegalArgumentException("Player has no aliases: " + id);
		return innerMap;
	}

	@Override
	public void deleteAlias(Player player, String aliasName) throws IOException {
		Map<String, String> innerMap = getInnerMap(player);
		innerMap.remove(aliasName);
	}

	@Override
	public List<AliasDescription> getAliases(Player player) {
		Map<String, String> innerMap = getInnerMap(player);
		List<AliasDescription> descriptions = new ArrayList<>(innerMap.values().size());
		for (Entry<String, String> entry : innerMap.entrySet()) {
			AliasDescriptionImpl d = new AliasDescriptionImpl(entry.getKey());
			descriptions.add(d);
		}
		return descriptions;
	}

}
