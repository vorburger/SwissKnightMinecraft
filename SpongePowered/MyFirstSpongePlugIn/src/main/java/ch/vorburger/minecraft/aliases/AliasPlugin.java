package ch.vorburger.minecraft.aliases;

import java.util.Map;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.collect.MapMaker;
import com.google.inject.Inject;

import ch.vorburger.minecraft.command.AbstractHotPluginWithCommands;
import ch.vorburger.minecraft.command.Command;
import ch.vorburger.minecraft.command.CommandRegistery;

@Plugin(id = "VAliases", name = "Aliasing commands", version = "1.0")
public class AliasPlugin extends AbstractHotPluginWithCommands {

	// TODO Persistence.. save the aliases, to survive restarts!
	
	private @Inject Game game;
	private @Inject CommandRegistery commandRegistery;
	private CommandService commandService; // cannot @Inject this :(

	protected Map<String, Script> aliases = new MapMaker().makeMap();
	
	@Command(value = "repeat command N times", aliases = { "n", "repeat", "loop" })
	public void x(CommandSource commandSource, int n, String commandToRepeat) {
		if (commandToRepeat.startsWith("/"))
			commandToRepeat = commandToRepeat.substring(1);
		for (int i = 0; i < n; i++) {
			// TODO use same impl. as alias, to be able to use Script multi-commands here also (and for same error handling)
			commandService.process(commandSource, commandToRepeat);
			// TODO handle returned result.. but doing above change will already take care of this, differently
		}
	}

	@Command("register a new Alias command")
	public void alias(CommandSource commandSource, String name, String commandsToAlias) {
		Script script = new Script(/*name, */commandsToAlias);
		commandRegistery.registerCommand(plugin, name, script.getCommands().toString(), script.asCommandExecutor(commandService));
		aliases.put(name, script);
	}
	
	@Command("list all available Aliases commands")
	public void alias(CommandSource commandSource) {
		commandSource.sendMessage(Texts.of(aliases.keySet().toString()));
	}

	@Command("delete an Alias command")
	public void alias(CommandSource commandSource, String name) {
		if (aliases.remove(name) != null)
			commandRegistery.unregister(name);
	}

	@Override
	protected void onStop(GameEvent event) {
		commandRegistery.unregisterAll();
		super.onStop(event);
	}
	
	@Override
	protected void onLoaded(GameEvent event) {
		super.onLoaded(event);
		this.commandService = game.getCommandDispatcher();
	}
}
