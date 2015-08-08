package ch.vorburger.minecraft.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.InvocationCommandException;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class CommandRegistery {

	private @Inject Logger logger;
	private @Inject Game game;

	private List<CommandMapping> commandMappings = new ArrayList<CommandMapping>();

	public void registerCommand(PluginContainer plugin, String name, String commandDescription, CommandExecutor commandExecutor) {
		registerCommand(plugin, Collections.singletonList(name), commandDescription, commandExecutor);
	}

	public void registerCommand(PluginContainer plugin, final List<String> commandNameAndAliases, String commandDescription, final CommandExecutor commandExecutor)  {
		final CommandCallable spec = CommandSpec.builder().description(Texts.of(commandDescription))
				// .arguments(GenericArguments.onlyOne(GenericArguments.world(Texts.of(ARG_WORLD), game)))
				.executor(commandExecutor).build();

		final Optional<CommandMapping> newCommand = game.getCommandDispatcher().register(plugin, spec, commandNameAndAliases);
		if (!newCommand.isPresent()) {
			logger.error(commandNameAndAliases + " command could not be registered! :-(");
		} else {
			synchronized (this) {
				commandMappings.add(newCommand.get());	
			}
		}
	}
	
	public void registerCommand(PluginContainer plugin, final List<String> commandNameAndAliases,
			String commandDescription,
			final CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable) 
	{
		registerCommand(plugin, commandNameAndAliases, commandDescription, new CommandExecutor() {
							public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
								try {
									commandExecutorWithoutResultThrowsThrowable.execute(src, args);
									return CommandResult.success();
								} catch (Throwable e) {
									final String msg = "/" + commandNameAndAliases + " failed: " + e.getMessage();
									logger.error(msg, e);
									throw new InvocationCommandException(Texts.builder(msg).build(), e);
								}
							}
						});
	}

	public void registerCommand(PluginContainer plugin, String name, String commandDescription, CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable) {
		registerCommand(plugin, Collections.singletonList(name), commandDescription, commandExecutorWithoutResultThrowsThrowable);
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
