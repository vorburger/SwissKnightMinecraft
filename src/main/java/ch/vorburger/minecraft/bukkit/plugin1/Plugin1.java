package ch.vorburger.minecraft.bukkit.plugin1;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin1 extends JavaPlugin {

	@Override
    public void onEnable() {
		getLogger().info("hello, world.  SwissKnightFirstBukky is running! More to come. Watch this space..");

		getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(), this);
    }

}
