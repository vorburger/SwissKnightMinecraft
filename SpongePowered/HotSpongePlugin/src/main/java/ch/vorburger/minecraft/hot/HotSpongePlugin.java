package ch.vorburger.minecraft.hot;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.plugin.PluginLoadedEvent;
import org.spongepowered.api.event.plugin.PluginUnloadingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Plugin(id = "HotSpongePlugIn", name = "HOT Sponge Plug-In", version = "1.0")
public class HotSpongePlugin {

	@Inject	Game game;
	@Inject PluginContainer plugin;
	@Inject Logger logger;

	Optional<CommandMapping> commandMapping = Optional.absent();

	@Subscribe
	public void onPluginLoaded(PluginLoadedEvent event) {
		if (!event.getPluginContainer().equals(plugin))
			return;
		logger.info("I'm loaded!");
		
		CommandSpec myCommandSpec = CommandSpec.builder()
			    .description(Texts.of("Hello World Command"))
			    .executor(new HelloWorldCommand())
			    .build();commandMapping=game.getCommandDispatcher().register(plugin,myCommandSpec,"hello");
	}


	@Subscribe
	public void onPluginUnloading(PluginUnloadingEvent event) {
		if (!event.getPluginContainer().equals(plugin))
			return;

		if (commandMapping.isPresent()) {
			game.getCommandDispatcher().removeMapping(commandMapping.get());
		}
	}

}
