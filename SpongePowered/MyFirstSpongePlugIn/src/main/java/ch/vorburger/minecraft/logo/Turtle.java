package ch.vorburger.minecraft.logo;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

public class Turtle {

	// TODO How to, optionally, "connect" this to a Player??
	
	Location location;
	Vector3i direction; // AKA "rotation"
	BlockType blockType;
	boolean isPenDown;

	Turtle(Location location, Vector3d direction, BlockType blockType) {
		this.location = location;
		this.direction = direction.toInt();
		this.blockType = BlockTypes.STONE;
		this.isPenDown = true;
	}
	
	Turtle(Location location, Vector3d direction) {
		this(location, direction, BlockTypes.STONE);
	}
	
	void changeBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	// ---

	void forward() {
		move(direction);
	}

	void back() {
		move(direction.negate()); // TODO
	}

	void right() {		
		// TODO direction = direction. how to?
	}

	void left() {
		// TODO
	}
		
	void up() {
		// TODO move(direction.negate());
	}

	void down() {
		// TODO move(direction.negate());
	}

	private void move(Vector3i moveDirection) {
		Vector3i newBlockPosition = location.getBlockPosition().add(moveDirection);
		location = new Location(location.getExtent(), newBlockPosition);
		setBlockIfPenDown();
	}

	private void setBlockIfPenDown() {
		if (isPenDown)
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
