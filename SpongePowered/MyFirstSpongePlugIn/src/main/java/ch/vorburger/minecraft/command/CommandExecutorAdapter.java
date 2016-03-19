package ch.vorburger.minecraft.command;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.InvocationCommandException;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

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
			throw new InvocationCommandException(Text.of(msg), e);
		}
	}

}
