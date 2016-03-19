package ch.vorburger.minecraft.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

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
			builder.description(Text.of(commandDescription));
		final CommandCallable spec = builder.arguments(args).executor(commandExecutor).build();

		final Optional<CommandMapping> newCommand = game.getCommandManager().register(plugin, spec, commandNameAndAliases);
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
		return registerCommand(plugin, commandNameAndAliases, commandDescription, CommandExecutorAdapter.adapt(commandExecutorWithoutResultThrowsThrowable), args);
	}

	public Optional<CommandMapping> createCommand(PluginContainer plugin, String name, String commandDescription, CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable, CommandElement... args) {
		return createCommand(plugin, Collections.singletonList(name), commandDescription, commandExecutorWithoutResultThrowsThrowable, args);
	}

}
