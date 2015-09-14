package ch.vorburger.minecraft.command;

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
import org.spongepowered.api.util.command.args.CommandElement;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.spongepowered.api.util.command.spec.CommandSpec.Builder;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;

public class CommandHelper {

	protected @Inject Logger logger;
	protected @Inject Game game;
	
	public Optional<CommandMapping> createCommand(PluginContainer plugin, String name, String commandDescription, CommandExecutor commandExecutor) {
		return registerCommand(plugin, Collections.singletonList(name), commandDescription, commandExecutor);
	}

	public Optional<CommandMapping> registerCommand(PluginContainer plugin, final List<String> commandNameAndAliases, String commandDescription, final CommandExecutor commandExecutor, CommandElement... args)  {
		final Builder builder = CommandSpec.builder();
		if (!Strings.isNullOrEmpty(commandDescription))
			builder.description(Texts.of(commandDescription));
		final CommandCallable spec = builder.arguments(args).executor(commandExecutor).build();

		final Optional<CommandMapping> newCommand = game.getCommandDispatcher().register(plugin, spec, commandNameAndAliases);
		if (!newCommand.isPresent()) {
			logger.error(commandNameAndAliases + " command could not be registered! :-(");
		}
		return newCommand;
	}
	
	public Optional<CommandMapping> createCommand(PluginContainer plugin, final List<String> commandNameAndAliases,
			String commandDescription,
			final CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable,
			CommandElement... args) 
	{
		return registerCommand(plugin, commandNameAndAliases, commandDescription, new CommandExecutor() {
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
		}, args);
	}

	public Optional<CommandMapping> createCommand(PluginContainer plugin, String name, String commandDescription, CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable, CommandElement... args) {
		return createCommand(plugin, Collections.singletonList(name), commandDescription, commandExecutorWithoutResultThrowsThrowable, args);
	}

}
