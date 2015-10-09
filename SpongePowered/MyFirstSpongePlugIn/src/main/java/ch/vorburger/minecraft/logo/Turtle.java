package ch.vorburger.minecraft.logo;

import java.util.NoSuchElementException;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;

/**
 * Turtle.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Logo_(programming_language)">Logo (programming language) on Wikipedia</a>
 * @see <a href="http://computercraft.info/wiki/Turtle_(API)">Computer Craft Turtle API</a> 
 * 
 * @author Michael Vorburger
 */
public class Turtle {

	Location location;
	Direction direction;
	BlockType blockType;
	boolean isSettingBlockOnMove = true;

	Turtle(Location location, Direction direction, BlockType blockType) {
		this.location = location;
		this.direction = direction;
		this.blockType = blockType;
	}
	
	public Turtle(Entity player) {
		this.location = getStartingLocation(player);
		this.direction = getDirection(player.getRotation());
		// TODO how to obtain Player's current Block? (+ Separate constructors for Entity & Player.)
		this.blockType = BlockTypes.STONE;
	}

	@SuppressWarnings("unchecked")
	private Location getStartingLocation(Entity entity) {
		// TODO How to get the Location from where the Player is looking at?
		// https://bukkit.org/threads/tutorial-how-to-calculate-vectors.138849/ ?

		Optional<BlockRayHit> block = Optional.absent();
		try {
			block = BlockRay.from(entity).filter(BlockRay.ONLY_AIR_FILTER, BlockRay.maxDistanceFilter(entity.getLocation().getPosition(), 100)).end();
		} catch (NoSuchElementException e) {
			// Caused by: java.util.NoSuchElementException: Filter limit reached
		}
		if (block.isPresent()) {
			return block.get().getLocation();
		} else {
			System.err.println("BlockRay hasn't found anything, return Player location"); // TODO remove
			return entity.getLocation();
		}
		
		// TODO Forum post, this doesn't quite work, it's always "off" and too close... https://forums.spongepowered.org/t/how-to-get-the-location-the-player-is-looking-at-from-an-entity-location-and-rotation/8906
		// TODO Try this https://forums.spongepowered.org/t/jumppad-plugin/6244 ??
		// TODO double check impact of "BTW, right now SpongeAPI returns the rotations in a Vector3i with the mapping X -> yaw, Y -> pitch, Z -> roll. I'm changing that tomorrow to X -> pitch, Y -> yaw, Z -> roll to match flow-math (and the standard)." of https://forums.spongepowered.org/t/relative-teleportaion/7671/14
//		Location location = entity.getLocation();
//		Vector3d rotation = entity.getRotation();
//        Vector3d direction = Quaterniond.fromAxesAnglesDeg(rotation.getY(), 360 - rotation.getX(), rotation.getZ()).getDirection();
//        return new Location(location.getExtent(), location.getPosition().add(direction)); 
	}

	private Direction getDirection(Vector3d rotation) {
		Direction initialDirection = Direction.getClosestHorizonal(rotation);
		return initialDirection;
	}

	// ---

	void changeBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	// ---
	
	void setBlockOnMove() {
		isSettingBlockOnMove = true;
	}
	
	void noSetBlockOnMove() {
		isSettingBlockOnMove = false;
	}
	
	// ---

	void turnRight() {
		switch (direction) {
		case NORTH:
			direction = Direction.EAST; 
			break;
		case EAST:
			direction = Direction.SOUTH; 
			break;
		case SOUTH:
			direction = Direction.WEST; 
			break;
		case WEST:
			direction = Direction.NORTH; 
			break;
		default:
			throw new IllegalStateException(direction.toString());
		}
		onChangeDirection(direction);
	}

	void turnLeft() {
		switch (direction) {
		case NORTH:
			direction = Direction.WEST; 
			break;
		case EAST:
			direction = Direction.NORTH; 
			break;
		case SOUTH:
			direction = Direction.EAST; 
			break;
		case WEST:
			direction = Direction.SOUTH; 
			break;
		default:
			throw new IllegalStateException(direction.toString());
		}
		onChangeDirection(direction);
	}
		
	protected void onChangeDirection(Direction newDirection) {
	}

	/** W */
	void moveForward() {
		move(direction);
	}

	/** S */
	void moveBack() {
		move(direction.getOpposite());
	}

	/** Space */
	void moveUp() {
		move(Direction.UP);
	}

	/** Shift */
	void moveDown() {
		move(Direction.DOWN);
	}

	private void move(Direction directionToMove) {
		setBlockIfPenDown();
		Vector3i oldBlockPosition = location.getBlockPosition();
		// TODO For performance, it would be better if Direction class had a toVector3i 
		Vector3i newBlockPosition = oldBlockPosition.add(directionToMove.toVector3d().toInt());
		location = new Location(location.getExtent(), newBlockPosition);
		onMove(location);
	}

	protected void onMove(Location newLocation) {
	}

	private void setBlockIfPenDown() {
		if (isSettingBlockOnMove)
			set();
	}

	// ---
	
	void set() {
		location.setBlockType(blockType);
	}
	
	void remove() {
		location.removeBlock();
	}

//	void interact() {
//		// TODO missing Direction arg, req. in latest 2.1-SNAPSHOT ?
//		location.interactBlock();
//	}
	
	void dig() {
		location.digBlock();
	}

}
