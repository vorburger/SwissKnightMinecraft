package ch.vorburger.minecraft.command;

import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

/**
 * CommandExecutor with throws Throwable instead of CommandException. 
 * @see CommandExecutor
 */
public interface CommandExecutorWithoutResultThrowsThrowable {

	void execute(CommandSource src, CommandContext args) throws Throwable;
	
}
