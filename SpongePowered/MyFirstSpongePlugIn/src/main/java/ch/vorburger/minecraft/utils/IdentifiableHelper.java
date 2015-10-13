package ch.vorburger.minecraft.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.spongepowered.api.Server;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.command.source.LocatedSource;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

/**
 * Helper to work with Identifiable objects.
 * 
 * If your plugin dynamically creates new in-Game objects, such as spawns new
 * Entities (Equipable), Worlds, Tasks, etc. perhaps in response to a Player's
 * /command, and needs to keep track of such objects across Player login/logout
 * and server restarts, this helper utility class is useful.
 * 
 * @author Michael Vorburger
 */
public class IdentifiableHelper {

	private static final String SCHEME_LOCATION = "loc";
	private static final String SCHEME_ENTITY = "entity";
	
	private final Server server;
	
	public IdentifiableHelper(Server server) {
		super();
		this.server = server;
	}

	public String getID(Entity entity) {
		String worldUUID = entity.getWorld().getUniqueId().toString();
		String entityUUID = entity.getUniqueId().toString();
		return newID(SCHEME_ENTITY, worldUUID, '/', entityUUID);
	}
	
	public String getID(Location<World> location) {
		World world = location.getExtent();
		String worldUUID = world.getUniqueId().toString();
		Vector3i v3i = location.getBlockPosition();
		String v3iString = toString(v3i);
		return newID(SCHEME_LOCATION, worldUUID, '/', v3iString);
	}

	public String getID(LocatedSource locatedSource) {
		if (locatedSource instanceof Entity) {
			Entity entity = (Entity)locatedSource;
			return getID(entity);
		} else {
			Location<World> location = locatedSource.getLocation();
			return getID(location);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Entity> Optional<T> findEntity(String id) {
		String[] parts = fromID(id);
		if (!SCHEME_ENTITY.equals(parts[0]))
			throw new IllegalArgumentException("Wrong scheme: " + id);
		String worldUUID = parts[1];
		String entityUUID = parts[2];
		
		Optional<World> optionalWorld = server.loadWorld(worldUUID);
		Optional<Entity> optionalEntity = optionalWorld.flatMap(world -> head(world.getEntities(entity -> entity.getUniqueId().equals(entityUUID))));
		return (Optional<T>) optionalEntity;
	}
	// TODO Entity findOrCreateEntity(String id, Supplier<Entity> factory) ?

	public Optional<LocatedSource> findLocatedSource(String id) {
		String[] parts = fromID(id);
		if (SCHEME_ENTITY.equals(parts[0])) {
			// The Entity must be a Player or Minecart LocatedSource, so cast should be safe
			// TODO propose a new interface LocatedSourceEntity extends LocatedSource and Entity which both Player and Minecart implement to make this easier  
			Optional<Entity> optionalEntity = findEntity(id);
			if (optionalEntity.isPresent()) {
				Entity entity = optionalEntity.get();
				LocatedSource locatedSourceEntity = (LocatedSource) entity;
				return Optional.of(locatedSourceEntity);
			} else {
				return Optional.empty();
			}
		} else {
			if (!SCHEME_LOCATION.equals(parts[0]))
				throw new IllegalArgumentException("Wrong scheme: " + id);
			String worldUUID = parts[1];
			Optional<World> optionalWorld = server.loadWorld(worldUUID);
			String v3iString = parts[2];
			Vector3i v3i = parseVector3i(v3iString);
			// TODO optionalWorld.flatMap(world -> world.get... ?)
			throw new UnsupportedOperationException("TODO");
		}
	}

	protected String toString(Vector3i v3i) {
		// copy/paste from com.flowpowered.math.vector.Vector3i.toString()
		// but duplicated, just in case that implementation ever changes
		return "(" + v3i.getX() + ", " + v3i.getY() + ", " + v3i.getZ() + ")";
	}

	protected Vector3i parseVector3i(String vector3iToString) {
		if (!vector3iToString.startsWith("(") || !vector3iToString.endsWith(")"))
			throw new IllegalArgumentException("Not a Vector3i: " + vector3iToString);
		vector3iToString = vector3iToString.substring(1, vector3iToString.length() - 1);
		String[] parts = vector3iToString.split(", ");
		return new Vector3i(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}

	protected String newID(String scheme, String part1, char separator, String part2) {
		return new StringBuilder(scheme.length() + 1 + part1.length() + 1 + part2.length()).
				append(scheme).append(':').append(part1).append('/').append(part2).toString();		
	}

	protected String[] fromID(String id) {
		int s = id.indexOf(':');
		if (s == -1)
			throw new IllegalArgumentException("No scheme: " + id);
		int p = id.indexOf('/');
		if (p == -1)
			throw new IllegalArgumentException("No / parts separator: " + id);
		String[] parts = new String[3];
		parts[0] = id.substring(0, s);
		parts[1] = id.substring(s + 1, p);
		parts[2] = id.substring(p + 1);
		return parts;
	}

	private <T> Optional<T> head(Collection<T> collection) {
		Iterator<T> iterator = collection.iterator();
		if (iterator.hasNext())
			return Optional.of(iterator.next());
		else
			return Optional.empty();
	}
	
/*	
	public ID newID(Entity entity, String suffix) {
		return new IDImpl("EntityWithSuffix", getEntityID(entity) + suffix);
	}

	public void newID(LocatedSource locatedSource, String suffix) {
		return new IDImpl("EntityWithSuffix", getEntityID(entity) + suffix);
	}

	public interface ID {
		String toString();
	}
	
	protected static class IDImpl implements ID {
		private static final String TO_STRING_PREFIX = ID.class.getName(); // IdentifiableHelper.class.getSimpleName() + "." + ID.class.getSimpleName();
		protected final String id;
		protected IDImpl(String scheme, String id) {
			this.id = new StringBuilder(scheme.length() + 1 + id.length()).append(scheme).append(':').append(id).toString();
		}
		@Override
		public String toString() {
			return new StringBuilder(id.length() + TO_STRING_PREFIX.length() + 1).append(TO_STRING_PREFIX).append(id).toString();
		}
		protected String getID() {
			return id;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IDImpl other = (IDImpl) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
*/
}
