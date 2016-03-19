package ch.vorburger.minecraft.aliases;

import java.util.Collections;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

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
		//		if (commands.contains(";"))
		//			return split(commands, ';');
		/*else */if (commands.contains("/"))
			return split(commands, '/');
		//		else if (commands.contains(","))
		//			return split(commands, ',');
		else
			return Collections.singletonList(commands);
	}

	private List<String> split(String string, char on) {
		return Collections.unmodifiableList(Splitter.on(on).omitEmptyStrings().trimResults().splitToList(string));
	}

	public CommandExecutor asCommandExecutor(final CommandManager commandService) {
		return new CommandExecutor() {
			@Override
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
