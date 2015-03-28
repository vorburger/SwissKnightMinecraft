package ch.vorburger.minecraft.bukkit.plugin1;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveEventListener implements Listener {

	@EventHandler
    public void onLogin(PlayerLoginEvent event) {
        ItemStack item1 = new ItemStack(Material.WOOD, 32);
        ItemStack item2 = new ItemStack(Material.COBBLESTONE, 32);
        ItemStack item3 = new ItemStack(Material.COAL, 32);
        ItemStack item4 = new ItemStack(Material.IRON_INGOT, 32);
		event.getPlayer().getInventory().addItem(item1, item2, item3, item4);
    }

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location loc = event.getPlayer().getLocation(); // Sets loc to five above where it used to be. Note that this doesn't change the player's position.
	    loc.setY(loc.getY() + 5);                       // Gets the block at the new location.
	    Block b = loc.getBlock();                       // Sets the block to type id 1 (stone).
	    b.setType(Material.STONE);
	}

}
