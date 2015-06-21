package ch.vorburger.minecraft.bukkit.plugin1

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveEventListener implements Listener {
	
	@EventHandler
	def onPlayerMove(PlayerMoveEvent event) {
		val loc = event.player.location   // Sets loc to five above where it used to be. Note that this doesn't change the player's position.
	    loc.y = loc.y + 5                 // Gets the block at the new location.
	    
	    loc.block.type = Material.STONE   // Sets the block to type id 1 (stone).
	}
	
}