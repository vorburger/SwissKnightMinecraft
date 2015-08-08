package ch.vorburger.minecraft.logo;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

public class Turtle {

	// TODO How to, optionally, "connect" this to a Player??
	
	private final static Vector3i Vector3i0   = new Vector3i( 0, -1, 0);
	private final static Vector3i Vector3i90  = new Vector3i(-1,  0, 0);
	private final static Vector3i Vector3i180 = new Vector3i( 0,  1, 0);
	private final static Vector3i Vector3i270 = new Vector3i( 1,  0, 0);
	
	Location location;
	Vector3i direction;
	BlockType blockType;
	boolean isSettingBlockOnMove = true;

	Turtle(Location location, Vector3d rotation, BlockType blockType) {
		this.location = location;
		this.direction = getDirectionFromRotation(rotation);
		this.blockType = blockType;
	}
	
	private Vector3i getDirectionFromRotation(Vector3d rotation) {
		// https://bukkit.org/threads/tutorial-how-to-calculate-vectors.138849/
		int yaw = rotation.getFloorX(); // angle the player is rotating horizontally in, in degrees
		// int pitch = rotation.getFloorY(); // angle up/down the player is looking in, in degrees
		
		yaw = Math.abs(yaw);
System.out.println(yaw);

		if (yaw < 44) return Vector3i0;
		else if (yaw < 134) return Vector3i90;
		else if (yaw < 224) return Vector3i180;
		else return Vector3i270;
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
		move(direction.negate());
	}

	/** Space */
	void moveUp() {
		// TODO move(direction.negate());
	}

	/** Shift */
	void moveDown() {
		// TODO move(direction.negate());
	}

	private void move(Vector3i moveDirection) {
		Vector3i oldBlockPosition = location.getBlockPosition();
System.out.println(oldBlockPosition);
		Vector3i newBlockPosition = oldBlockPosition.add(moveDirection);
		location = new Location(location.getExtent(), newBlockPosition);
System.out.println(location);
		setBlockIfPenDown();
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
