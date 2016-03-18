package ch.vorburger.minecraft.ideas;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Container {

	boolean isInside(Location<World> loc);

	Container getParent();
	Iterable<Container> getChildren();


	public interface Cube extends Container {

		Location<World> p1();
		Location<World> p2();

	}
}
