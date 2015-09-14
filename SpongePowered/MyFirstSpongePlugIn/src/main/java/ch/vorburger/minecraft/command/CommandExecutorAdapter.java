package ch.vorburger.minecraft.command;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.InvocationCommandException;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class CommandExecutorAdapter implements CommandExecutor {

	private final CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable;

	public static CommandExecutor adapt(CommandExecutorWithoutResultThrowsThrowable executor) {
		return new CommandExecutorAdapter(executor);
	}
	
	private CommandExecutorAdapter(CommandExecutorWithoutResultThrowsThrowable commandExecutorWithoutResultThrowsThrowable) {
		super();
		this.commandExecutorWithoutResultThrowsThrowable = commandExecutorWithoutResultThrowsThrowable;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		try {
			commandExecutorWithoutResultThrowsThrowable.execute(src, args);
			return CommandResult.success();
		} catch (Throwable e) {
			final String msg = "Commmand failed: " + e.getMessage(); // "/" + commandNameAndAliases + " failed: " + e.getMessage();
			// logger.error(msg, e);
			throw new InvocationCommandException(Texts.builder(msg).build(), e);
		}
	}

}
