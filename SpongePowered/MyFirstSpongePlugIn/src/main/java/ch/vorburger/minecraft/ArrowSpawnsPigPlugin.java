package ch.vorburger.minecraft;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.animal.Pig;
import org.spongepowered.api.entity.projectile.Arrow;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.EntityMoveEvent;
import org.spongepowered.api.event.entity.EntitySpawnEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.common.base.Optional;

@Plugin(id = "ch.vorburger.minecraft.ArrowSpawnsPig", name = "Arrow spawns Pig", version = "1.0.0-SNAPSHOT")
public class ArrowSpawnsPigPlugin {

	// TODO use PassengerData instead of tracking like this
	
	// TODO List of Arrows, remove when it touches ground
	private Arrow trackedArrow;
	private Pig arrowsPig;

	@Subscribe
	public void onEntitySpawn(EntitySpawnEvent event) {
		Entity spawnedEntity = event.getEntity();
		if (spawnedEntity instanceof Arrow) {
			trackedArrow = (Arrow) spawnedEntity;
			World world = spawnedEntity.getWorld();
			Location location = spawnedEntity.getLocation();
			Optional<Entity> optional = world.createEntity(EntityTypes.PIG, location.getPosition());
			if (optional.isPresent()) {
				Pig pig = (Pig) optional.get();
				world.spawnEntity(pig);
				arrowsPig = pig;
				System.out.println("Spawned pig at: " + location.getPosition().toString());
			}
		}
	}

//	@Subscribe
//	public void onEntityMove(EntityMoveEvent event) {
//		if (event.getEntity().equals(trackedArrow)) {
//			arrowsPig.setLocation(trackedArrow.getLocation());
//		}
//	}
	
}
