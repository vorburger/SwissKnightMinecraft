package ch.vorburger.minecraft.bukkit.plugin1

import org.bukkit.entity.Player
import org.bukkit.entity.EntityType

class FireRain {

	def makeFireRain(Player player) {
		val loc = player.location
		for (var i = 0; i < 10; i++) {
			loc.y = loc.y + 10
			loc.x = loc.x + 2
			loc.z = loc.z + i
			val ball = player.world.spawnEntity(loc, EntityType.FIREBALL)
			val velocity = ball.velocity
			velocity.x = 0
			velocity.z = 0
		}
	}

}