package ch.vorburger.minecraft.logo;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Turtle {

	Location<World> fwd();

	Location<World> back();

	void rt();

	void lt();

	Location<World> up();

	Location<World> down();

	void setBlockType(BlockType blockType);

	void set();

	void remove();

	void setBlockOnMove();

	void noSetBlockOnMove();

	Location<World> getLocation();

}
