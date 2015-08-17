// TODO LICENSE
package ch.vorburger.minecraft.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;
import org.spongepowered.api.util.command.args.GenericArguments;

import com.google.inject.Inject;

/**
 * TODO Doc
 * 
 * @author Michael Vorburger
 */
public class CommandManager {

	private @Inject Logger logger;
	private @Inject CommandRegistery commandRegistery;

	// @see org.spongepowered.common.event.SpongeEventManager.register(PluginContainer, Object)
	public void register(final PluginContainer plugin, final Object instanceOfClassWithCommandAnnotatedMethods) {
		final Class<?> handle = instanceOfClassWithCommandAnnotatedMethods.getClass();
        for (final Method method : handle.getMethods()) {
            Command commandAnnotation = method.getAnnotation(Command.class);
            if (commandAnnotation != null) {
                if (isValidHandler(method)) {
                	final String commandDescription = commandAnnotation.value();
                	final List<String> commandNameAndAliases = getCommandNameAndAliases(method);
                	final List<CommandElement> args = getCommandElements(method);
					commandRegistery.registerCommand(plugin, commandNameAndAliases, commandDescription, new CommandExecutorWithoutResultThrowsThrowable() {
            			public void execute(CommandSource src, CommandContext args) throws Throwable {
            				Player player = (Player) src; // TODO if instanceof
            				method.invoke(instanceOfClassWithCommandAnnotatedMethods, player);
            			}
            		}, args.toArray(new CommandElement[0]));
                } else {
                	logger.warn("The method {} on {} has @{} but has the wrong signature", method, handle.getName(), Command.class.getName());
                }
            }
        }
	}
	
	protected List<String> getCommandNameAndAliases(Method method) {
        final Command commandAnnotation = method.getAnnotation(Command.class);
    	final List<String> aliases = Arrays.asList(commandAnnotation.aliases());
    	final List<String> commandNameAndAliases = new ArrayList<String>(aliases.size() + 1);
    	commandNameAndAliases.add(method.getName());
    	commandNameAndAliases.addAll(aliases);
    	return commandNameAndAliases;
	}
	
	protected List<CommandElement> getCommandElements(Method method) {
		List<CommandElement> commandElements = new ArrayList<CommandElement>();
    	Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
	    	CommandElement commandElement;
	    	String name = parameter.getName();
	    	Type type = parameter.getParameterizedType();
	    	type.
	    	commandElement = GenericArguments.onlyOne(commandElement);
	    	commandElements.add(commandElement);
		}
		return commandElements;
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
