/*
 * This file is part of ch.vorburger.minecraft, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 Michael Vorburger <http://www.vorburger.ch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.vorburger.minecraft.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;
import org.spongepowered.api.util.command.args.GenericArguments;

import com.google.common.base.Optional;
import com.google.inject.Inject;

/**
 * TODO Doc
 * 
 * @author Michael Vorburger
 */
public class CommandManager {

	private @Inject Logger logger;
	private @Inject Game game;
	private @Inject CommandRegistery commandRegistery;

	// @see org.spongepowered.common.event.SpongeEventManager.register(PluginContainer, Object)
	public void register(final PluginContainer plugin, final Object instanceOfClassWithCommandAnnotatedMethods) {
		final Class<?> handle = instanceOfClassWithCommandAnnotatedMethods.getClass();
        for (final Method method : handle.getDeclaredMethods()) {
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

	protected static class MethodArg {
		String name;
		Type type;
		boolean optional = false;
	}

	protected List<MethodArg> getMethodArgs(Method method) {
		// TODO rewrite this in nice Java 8 functional "mapping" style
    	Parameter[] parameters = method.getParameters();
		List<MethodArg> args = new ArrayList<>(parameters.length);
		for (Parameter parameter : parameters) {
	    	if (!parameter.isNamePresent())
	    		// https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html
	    		throw new IllegalStateException("Needs javac -parameters; or, in Eclipse: 'Store information about method parameters (usable via reflection)' in Window -> Preferences -> Java -> Compiler");
	    	MethodArg arg = new MethodArg();
	    	args.add(arg);
	    	String name = parameter.getName();
	    	arg.name = name;
	    	Type type = parameter.getParameterizedType();
	    	if (type instanceof ParameterizedType) {
	    		ParameterizedType parameterizedType = (ParameterizedType) type;
	    		Type[] actualTypes = parameterizedType.getActualTypeArguments();
	    		Type rawType = parameterizedType.getRawType();
	    		if (Optional.class.isAssignableFrom((Class<?>)rawType)) {
    				arg.optional = true;
	    			arg.type = actualTypes[0];
	    		}
	    	} 
	    	if (arg.type == null) {
	    		arg.type = type;
	    	}
		}
		return args;
	}
	
	protected List<CommandElement> getCommandElements(Method method) {
		List<CommandElement> commandElements = new ArrayList<>(method.getParameterCount());
		for (MethodArg arg : getMethodArgs(method)) {
			Optional<CommandElement> optCommandElement = getCommandElement(arg.name, arg.type);
			if (!optCommandElement.isPresent())
				throw new IllegalArgumentException("@Command method " + method.toString() + " parameter unsupported type: " + arg.type);
	    	CommandElement commandElement = optCommandElement.get();
	    	if (arg.optional)
	    		commandElement = GenericArguments.optional(commandElement);
	    	commandElement = GenericArguments.onlyOne(commandElement);
	    	commandElements.add(commandElement);
		}
		return commandElements;
	}

	private Optional<CommandElement> getCommandElement(String name, Type type) {
    	if (type instanceof Class) {
    		Class<?> typeClass = (Class<?>) type;
    		if (Player.class.isAssignableFrom(typeClass)) {
    			return Optional.of(GenericArguments.player(Texts.of(name), game));
    		} else if (CommandSource.class.isAssignableFrom(typeClass)) {
    			return Optional.of(GenericArguments.playerOrSource(Texts.of(name), game));
    		} else if (String.class.isAssignableFrom(typeClass)) {
    			return Optional.of(GenericArguments.string(Texts.of(name)));    			
    		}
    	}
		return Optional.absent();
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
