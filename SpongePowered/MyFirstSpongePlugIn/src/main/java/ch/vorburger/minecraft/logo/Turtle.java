package ch.vorburger.minecraft.logo;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Turtle {

	Location<World> moveForward();

	Location<World> moveBack();

	void turnRight();

	void turnLeft();

	Location<World> moveUp();

	Location<World> moveDown();

	void setBlockType(BlockType blockType);

	void set();

	void remove();

	void setBlockOnMove();

	void noSetBlockOnMove();

}
