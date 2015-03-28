package ch.vorburger.minecraft.bukkit.plugin1;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerLoginEventListener implements Listener {

	@EventHandler
    public void onLogin(PlayerLoginEvent event) {
        ItemStack item1 = new ItemStack(Material.WOOD, 32);
        ItemStack item2 = new ItemStack(Material.COBBLESTONE, 32);
        ItemStack item3 = new ItemStack(Material.COAL, 32);
        ItemStack item4 = new ItemStack(Material.IRON_INGOT, 32);
		event.getPlayer().getInventory().addItem(item1, item2, item3, item4);
    }

}
