package ch.vorburger.minecraft.bukkit.plugin1;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin1 extends JavaPlugin {

	@Override
    public void onEnable() {
		getLogger().info("hello, world.  SwissKnightFirstBukky is running! More to come. Watch this space..");

		getServer().getPluginManager().registerEvents(new PlayerLoginEventListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(), this);
    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return false;
		}
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("firerain")) {
			new FireRain().makeFireRain(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("zeus")) {
			new zeus().makezeus(player);
			return true;
		} else 
			return false; 
	}
}
