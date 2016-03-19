package ch.vorburger.minecraft.aliases;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameStateEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import com.google.common.base.Joiner;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;
import ch.vorburger.minecraft.command.CommandExecutorAdapter;
import ch.vorburger.minecraft.command.CommandHelper;
import ch.vorburger.minecraft.command.CommandRegistry;

/**
 * Aliases. For example:
 *    /x 4 fd
 * @author Michael Vorburger
 */
//@Plugin(id = "VAliases", name = "Aliasing commands", version = "1.0")
public class AliasPlugin extends AbstractHotPlugin {
	protected @Inject Logger logger;

	// TODO Persistence.. save the aliases, to survive restarts!

	protected @Inject Game game;
	protected @Inject PluginContainer plugin;
	protected @Inject CommandRegistry commandRegistry;
	protected @Inject CommandHelper commandHelper;
	protected CommandManager commandService; // cannot @Inject this :(

	protected Map<String, Script> aliases = new MapMaker().makeMap();

	protected static final Joiner joiner = Joiner.on(' ');

	// @Command(value = "repeat command N times", aliases = { "n", "repeat", "loop" })
	public void x(CommandSource commandSource, int n, String... commandsToRepeat) {
		String joinedCommandsToRepeat = joiner.join(commandsToRepeat);
		if (joinedCommandsToRepeat.startsWith("/"))
			joinedCommandsToRepeat = joinedCommandsToRepeat.substring(1);
		for (int i = 0; i < n; i++) {
			// TODO use same impl. as alias, to be able to use Script multi-commands here also (and for same error handling)
			commandService.process(commandSource, joinedCommandsToRepeat);
			// TODO handle returned result.. but doing above change will already take care of this, differently
		}
	}

	// @Command("list all / register new / delete Alias command")
	public void alias(CommandSource commandSource, Optional<String> name, Optional<String> commandsToAlias) {
		if (name.isPresent() && commandsToAlias.isPresent())
			alias(commandSource, name.get(), commandsToAlias.get());
		else if (name.isPresent() && !commandsToAlias.isPresent())
			alias(commandSource, name.get());
		else // !name.isPresent() && !commandsToAlias.isPresent()
			alias(commandSource);
	}

	//// @Command("register a new Alias command")
	public void alias(CommandSource commandSource, String name, String commandsToAlias) {
		Script script = new Script(/*name, */commandsToAlias);
		Optional<CommandMapping> command = commandHelper.createCommand(plugin, name, script.getCommands().toString(), script.asCommandExecutor(commandService));
		if (command.isPresent())
			commandRegistry.register(command.get());
		else
			// logger.error("Failed to create CommandMapping to register: " + name);
			throw new IllegalArgumentException("Failed to create CommandMapping to register: " + name);
		aliases.put(name, script);
	}

	//// @Command("list all available Aliases commands")
	public void alias(CommandSource commandSource) {
		// TODO Format more nicely!
		if (!aliases.keySet().isEmpty())
			commandSource.sendMessage(Text.of(aliases.keySet().toString()));
	}

	//// @Command("delete an Alias command")
	public void alias(CommandSource commandSource, String name) {
		if (aliases.remove(name) != null)
			commandRegistry.unregister(name);
	}

	@Override
	protected void onStop(GameStateEvent event) {
		commandRegistry.unregisterAll();
	}

	@Override
	protected void onLoaded(GameStateEvent event) {
		this.commandService = game.getCommandManager();

		commandRegistry.register(CommandSpec.builder().description(Text.of("repeat command N times")).arguments(
				// GenericArguments.playerOrSource(Text.of("player"), game)
				GenericArguments.integer(Text.of("n")),
				GenericArguments.allOf(GenericArguments.string(Text.of("commandToRepeat")))
				).executor(CommandExecutorAdapter.adapt((src, args) -> {
					// Player player = (Player) src; // TODO if instanceof
					Integer n = args.<Integer>getOne("n").get();
					Collection<String> commandsToRepeat = args.<String>getAll("commandToRepeat");
					this.x(src, n, commandsToRepeat.toArray(new String[0]));
				})).build(), "x");

		commandRegistry.register(CommandSpec.builder().description(Text.of("list all / register new / delete Alias command")).arguments(
				// GenericArguments.playerOrSource(Text.of("player"), game)
				GenericArguments.optional(GenericArguments.string(Text.of("cmd"))),
				GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("commandsToAlias")))
				).executor(CommandExecutorAdapter.adapt((src, args) -> {
					// Player player = (Player) src; // TODO if instanceof
					Optional<String> name = args.<String>getOne("cmd");
					Optional<String> commandsToAlias = args.<String>getOne("commandsToAlias");
					this.alias(src, name, commandsToAlias);
				})).build(), "def", "alias");

	}
}
