package ch.vorburger.minecraft.command;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

import ch.vorburger.minecraft.hot.AbstractHotPlugin;

public abstract class AbstractPluginWithCommands extends AbstractHotPlugin {

	protected @Inject PluginContainer plugin;

	private @Inject AnnotatedCommandManager commandManager;

	protected void onServerStarting() {
	}

	protected void onServerStopping() {
	}

	@Override
	@Listener
	public final void onServerStarting(GameStartingServerEvent event) {
		super.onServerStarting(event);
		commandManager.register(plugin, this);
		onServerStarting();
	}

	@Override
	@Listener
	public final void onServerStopping(GameStoppingServerEvent event) {
		super.onServerStopping(event);
		commandManager.unregisterAll();
		onServerStopping();
	}

}
