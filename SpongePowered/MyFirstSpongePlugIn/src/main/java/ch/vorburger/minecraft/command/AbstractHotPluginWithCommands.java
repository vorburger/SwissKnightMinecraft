package ch.vorburger.minecraft.command;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

public abstract class AbstractHotPluginWithCommands {
	// TODO remove *Hot* from class name

	protected @Inject PluginContainer plugin;

	private @Inject CommandManager commandManager;

	protected void onServerStarting() {
	}

	protected void onServerStopping() {
	}

	@Listener
	public final void onServerStarting(GameStartingServerEvent event) {
		commandManager.register(plugin, this);
		onServerStarting();
	}

	@Listener
	public final void onServerStopping(GameStoppingServerEvent event) {
		commandManager.unregisterAll();
		onServerStopping();
	}

}
