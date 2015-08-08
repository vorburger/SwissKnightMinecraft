package ch.vorburger.minecraft.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

import com.google.inject.Inject;

public class CommandManager {

	private @Inject Logger logger;
	private @Inject CommandRegistery commandRegistery;

	// @see org.spongepowered.common.event.SpongeEventManager.register(PluginContainer, Object)
	public void register(final PluginContainer plugin, final Object instanceOfClassWithCommandAnnotatedMethods) {
		final Class<?> handle = instanceOfClassWithCommandAnnotatedMethods.getClass();
        for (final Method method : handle.getMethods()) {
            Command commandAnnotation = method.getAnnotation(Command.class);
            // TODO add support for an Aliases annotation, or an aliases String array on Command
            if (commandAnnotation != null) {
                if (isValidHandler(method)) {
                	final String commandDescription = commandAnnotation.value();
                	final List<String> aliases = Arrays.asList(commandAnnotation.aliases());
                	final List<String> commandNameAndAliases = new ArrayList<String>(aliases.size() + 1);
                	commandNameAndAliases.add(method.getName());
                	commandNameAndAliases.addAll(aliases);
                	// TODO handle diff. args - for now, it's hard-coded to support 1 Player arg., only.
                	commandRegistery.registerCommand(plugin, commandNameAndAliases, commandDescription, new CommandExecutorWithoutResultThrowsThrowable() {
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

	public void unregisterAll() {
		commandRegistery.unregisterAll();
	}
	
}
