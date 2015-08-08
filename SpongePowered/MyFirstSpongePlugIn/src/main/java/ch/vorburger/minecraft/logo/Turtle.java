package ch.vorburger.minecraft.logo;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

public class Turtle {

	// TODO How to, optionally, "connect" this to a Player??
	
	Location location;
	Direction direction;
	BlockType blockType;
	boolean isSettingBlockOnMove = true;

	Turtle(Location location, Vector3d rotation, BlockType blockType) {
		this.location = getStartingLocation(location, rotation);
		this.direction = getDirection(rotation);
		this.blockType = blockType;
	}
	
	private Location getStartingLocation(Location location, Vector3d rotation) {
		// TODO How to get a Location from where the Player is looking at?
		// https://bukkit.org/threads/tutorial-how-to-calculate-vectors.138849/ ?
		return location;
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
		// TODO direction = direction. how to?
	}

	void turnLeft() {
		// TODO
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
		// TODO move(direction.negate());
	}

	/** Shift */
	void moveDown() {
		// TODO move(direction.negate());
	}

	private void move(Direction directionToMove) {
		setBlockIfPenDown();
		Vector3i oldBlockPosition = location.getBlockPosition();
		// TODO LOW For performance, it would be better if Direction class had a toVector3i 
		Vector3i newBlockPosition = oldBlockPosition.add(directionToMove.toVector3d().toInt());
		location = new Location(location.getExtent(), newBlockPosition);
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

	void interact() {
		location.interactBlock();
	}
	
	void dig() {
		location.digBlock();
	}

}
