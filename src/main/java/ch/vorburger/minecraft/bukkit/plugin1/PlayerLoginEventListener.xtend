package ch.vorburger.minecraft.bukkit.plugin1

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.inventory.ItemStack

class PlayerLoginEventListener implements Listener {

	@EventHandler def void onLogin(PlayerLoginEvent event) {
		var ItemStack item1 = new ItemStack(Material.WOOD, 32)
		var ItemStack item2 = new ItemStack(Material.COBBLESTONE, 32)
		var ItemStack item3 = new ItemStack(Material.COAL, 32)
		var ItemStack item4 = new ItemStack(Material.IRON_INGOT, 32)
		event.player.inventory.addItem(item1, item2, item3, item4)
	}

}
