package ch.vorburger.minecraft.bukkit.plugin1

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.Listener

class PlayerMoveEventListener implements Listener {
	
	@EventHandler
	def onPlayerMove(PlayerMoveEvent event) {
		val Location loc = event.getPlayer().getLocation() // Sets loc to five above where it used to be. Note that this doesn't change the player's position.
	    loc.setY(loc.getY() + 5)                           // Gets the block at the new location.
	    val Block b = loc.getBlock()                       // Sets the block to type id 1 (stone).
	    b.setType(Material.STONE)
	}
	
}