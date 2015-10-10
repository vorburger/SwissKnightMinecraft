package ch.vorburger.minecraft.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.google.inject.Inject;

public class CommandRegistry {

	protected @Inject Logger logger;
	protected @Inject Game game;
	protected @Inject PluginContainer plugin;
	
	private List<CommandMapping> commandMappings = new ArrayList<CommandMapping>();

	synchronized public void register(CommandMapping commandMapping) {
		commandMappings.add(commandMapping);
	}
	
	synchronized public void unregister(String name) {
		for (CommandMapping commandMapping : commandMappings) {
			if (name.equals(commandMapping.getPrimaryAlias())) {
				game.getCommandDispatcher().removeMapping(commandMapping);
				commandMappings.remove(commandMapping);
			}
		}		
	}

	public void unregisterAll() {
		for (CommandMapping commandMapping : commandMappings) {
			game.getCommandDispatcher().removeMapping(commandMapping);
		}
		commandMappings.clear();		
	}
	
	// @see org.spongepowered.api.util.command.dispatcher.Dispatcher.getCommands()
	public List<CommandMapping> getCommands() {
		return Collections.unmodifiableList(commandMappings);
	}
	
	// ---
	
	public void register(CommandSpec commandSpec, String... commandNameAndAliases) {
		Optional<CommandMapping> optionalCommandMapping = game.getCommandDispatcher().register(plugin, commandSpec, commandNameAndAliases);
		if (optionalCommandMapping.isPresent()) {
			register(optionalCommandMapping.get());
		} else {
			logger.warn("Could not register new command with name/aliases: " + Arrays.toString(commandNameAndAliases));
		}
	}

}
