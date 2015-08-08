package ch.vorburger.minecraft.command;

import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;

public abstract class AbstractHotPluginWithCommands extends AbstractHotPlugin {

	protected @Inject PluginContainer plugin;

	private @Inject CommandManager commandManager;

	@Override
	protected void onLoaded(GameEvent event) {
		commandManager.register(plugin, this);
	}

	@Override
	protected void onStop(GameEvent event) {
		commandManager.unregisterAll();
	}

}
