package ch.vorburger.minecraft.command;

import org.spongepowered.api.event.game.state.GameStateEvent;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;

public abstract class AbstractHotPluginWithCommands extends AbstractHotPlugin {

	protected @Inject PluginContainer plugin;

	private @Inject CommandManager commandManager;

	@Override
	protected void onLoaded(GameStateEvent event) {
		commandManager.register(plugin, this);
	}

	@Override
	protected void onStop(GameStateEvent event) {
		commandManager.unregisterAll();
	}

}
