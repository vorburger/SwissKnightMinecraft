package ch.vorburger.minecraft.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
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

public class CommandManager {

	private @Inject Logger logger;
	private @Inject Game game;

	private List<CommandMapping> commandMappings = new ArrayList<CommandMapping>();

	// @see org.spongepowered.common.event.SpongeEventManager.register(PluginContainer, Object)
	public void register(final PluginContainer plugin, final Object instanceOfClassWithCommandAnnotatedMethods) {
		final Class<?> handle = instanceOfClassWithCommandAnnotatedMethods.getClass();
        for (final Method method : handle.getMethods()) {
            Command commandAnnotation = method.getAnnotation(Command.class);
            if (commandAnnotation != null) {
                if (isValidHandler(method)) {
                	final String commandDescription = commandAnnotation.value();
                	// TODO handle diff. args - for now, it's hard-coded to support 1 Player arg., only.
                	registerCommand(plugin, method.getName(), commandDescription, new CommandExecutorWithoutResultThrowsThrowable() {
            			public void execute(CommandSource src, CommandContext args) throws Throwable {
            				Player player = (Player) src; // TODO if instanceof
            				method.invoke(instanceOfClassWithCommandAnnotatedMethods, player);
            			}
            		});
                } else {
                	logger.warn("The method {} on {} has @{} but has the wrong signature", method, handle.getName(), Command.class.getName());
                }
            }
        }
	}
	
	private boolean isValidHandler(final Method method) {
		final int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers)
                || method.getDeclaringClass().isInterface()
                || method.getReturnType() != void.class) {
            return false;
        }
        return true;
	}

	public void unregister() {
		for (CommandMapping commandMapping : commandMappings) {
			game.getCommandDispatcher().removeMapping(commandMapping);
		}
		commandMappings.clear();
	}
	
	private void registerCommand(final PluginContainer plugin, final String commandName, String commandDescription, final CommandExecutorWithoutResultThrowsThrowable commandExecutorThrowsThrowable) {
		final CommandCallable spec = CommandSpec.builder().description(Texts.of(commandDescription))
			// .arguments(GenericArguments.onlyOne(GenericArguments.world(Texts.of(ARG_WORLD), game)))
			.executor(new CommandExecutor() {
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
					try {
						commandExecutorThrowsThrowable.execute(src, args);
						return CommandResult.success();
					} catch (Throwable e) {
						final String msg = "/" + commandName + " failed: " + e.getMessage();
						logger.error(msg, e);
						throw new InvocationCommandException(Texts.builder(msg).build(), e);
					}
				}
			})
			.build();

		final Optional<CommandMapping> newCommand = game.getCommandDispatcher().register(plugin, spec, commandName);
		if (!newCommand.isPresent()) {
			logger.error(commandName + " command could not be registered! :-(");
		} else {
			commandMappings.add(newCommand.get());	
		}
	}

}
