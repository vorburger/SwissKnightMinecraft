package ch.vorburger.minecraft.logo;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;
import ch.vorburger.minecraft.command.Command;
import ch.vorburger.minecraft.command.CommandManager;

@Plugin(id = "LogoPlugin", name = "Logo-like commands (thank you, Seymour Papert)", version = "1.0")
public class LogoPlugin extends AbstractHotPlugin {

	@Inject PluginContainer plugin;
	@Inject CommandManager commandManager;

	@Command("draw big cube")
	public void cube(Player player) {
		Turtle turtle = new Turtle(player);
		for (int h = 0; h < 3; h++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 5; j++) {
					turtle.moveForward();
				}
				turtle.turnRight();
			}
			turtle.moveUp();
		}
		// TODO For Dév, fill everything inside the house with Air.. eval. using /fill command!
	}
	
	@Override
	protected void onLoaded(GameEvent event) {
		commandManager.register(plugin, this);
	}

	@Override
	protected void onStop(GameEvent event) {
		commandManager.unregister();
	}

}
