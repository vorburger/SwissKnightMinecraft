package ch.vorburger.minecraft.logo;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.command.source.LocatedSource;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * {@link Turtle} that is connected to a visible Entity, moving that Entity
 * along.
 *
 * Note that an Entity with AI such as a Pig will tend to turn and move away,
 * and is thus not very suitable as a Turtle. A Human is a bit better, but also
 * not perfectly ideal because it obviously obeys gravity and thus falls (TODO
 * How-To make it Fly??). Also when the Turtle is within existing blocks
 * rendering can be weird.
 *
 * @author Michael Vorburger
 */
public class EntityConnectedTurtle extends Turtle {

	private Entity companion;

	public EntityConnectedTurtle(Entity turtleEntity, LocatedSource turtleEntitySpawnerForInitialLookingAtPositionAndDirection) {
		super(turtleEntitySpawnerForInitialLookingAtPositionAndDirection);
		this.companion = turtleEntity;
		onChangeDirection(this.direction);
		companion.setLocation(this.location);
	}

	@Override
	protected void onMove(Location<World> newLocation) {
		// setLocation is better/clearer than setLocationSafely, for a Turtle -
		// even if it can lead to weired visuals of Entity-in-Block, but that's
		// still preferable to the alternative of the Companion Entity not
		// showing clearly where the Turtle is.
		companion.setLocation(newLocation);
	}

	@Override
	protected void onChangeDirection(Direction newDirection) {
		// TODO Why does companion not turn correctly on e.g. /lt ??
		//System.out.print(companion.getRotation());
		companion.setRotation(newDirection.toVector3d());
		//System.out.print(newDirection.toVector3d());
		//System.out.println(companion.getRotation());
	}
}
