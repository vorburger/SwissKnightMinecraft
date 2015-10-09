package ch.vorburger.minecraft.logo;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

/**
 * {@link Turtle} that is connected to a visible Entity, moving that Entity along.
 *
 * @author Michael Vorburger
 */
public class EntityConnectedTurtle extends Turtle {

	private Entity companion;

	public EntityConnectedTurtle(Entity turtleEntity) {
		super(turtleEntity);
		this.companion = turtleEntity;
	}

	@Override
	protected void onMove(Location newLocation) {
		companion.setLocationSafely(newLocation);
	}
	
	@Override
	protected void onChangeDirection(Direction newDirection) {
		companion.setRotation(newDirection.toVector3d());
	}
}
