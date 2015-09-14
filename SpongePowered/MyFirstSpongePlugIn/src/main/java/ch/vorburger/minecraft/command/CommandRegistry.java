package ch.vorburger.minecraft.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spongepowered.api.Game;
import org.spongepowered.api.util.command.CommandMapping;

import com.google.inject.Inject;

public class CommandRegistry {

	// private @Inject Logger logger;
	protected @Inject Game game;

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

}
