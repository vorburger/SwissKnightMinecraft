package ch.vorburger.minecraft.aliases;

import java.util.Collections;
import java.util.List;

import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.google.common.base.Splitter;

public class Script {

	// private final String name;
	private final List<String> commands;
	
	public Script(/*String name, */String commands) {
		// this.name = name;
		this.commands = parse(commands);
		if (this.commands.isEmpty())
			throw new IllegalArgumentException("commands empty");
	}

//	public String getName() {
//		return name;
//	}

	public List<String> getCommands() {
		return commands;
	}

	private List<String> parse(String commands) {
		if (commands.contains(";"))
			return split(commands, ';');
		else if (commands.contains("/"))
			return split(commands, '/');
		else if (commands.contains(","))
			return split(commands, ',');
		else
			return Collections.singletonList(commands);
	}

	private List<String> split(String string, char on) {
		return Collections.unmodifiableList(Splitter.on(on).omitEmptyStrings().trimResults().splitToList(string));
	}

	public CommandExecutor asCommandExecutor(final CommandService commandService) {
		return new CommandExecutor() {
			public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
				CommandResult lastResult = null;
				for (String command : commands) {
					lastResult = commandService.process(src, command);
				}
				return lastResult;
			}
		};
	}
	
}
