package ch.vorburger.minecraft.logo;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.InvocationCommandException;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;
import ch.vorburger.minecraft.command.CommandExecutorWithoutResultThrowsThrowable;

@Plugin(id = "LogoPlugin", name = "Logo-like commands (thank you, Seymour Papert)", version = "1.0")
public class LogoPlugin extends AbstractHotPlugin {

	@Inject Game game;
	@Inject Logger logger;
	@Inject PluginContainer plugin;

	Optional<CommandMapping> testCommand;

	// TODO @Command("Just testing")
	public void test(Player player) {
		Turtle turtle = new Turtle(player);
		for (int h = 0; h < 3; h++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 10; j++) {
					turtle.moveForward();
				}
				turtle.turnRight();
			}
			turtle.moveUp();
		}
		// TODO For Dév, fill everything inside the house with Air.. eval. using /fill command!
	}
	

	@Override
	protected void onLoaded(GameEvent event) {
		testCommand = registerCommand("test", "Just testing", new CommandExecutorWithoutResultThrowsThrowable() {
			public void execute(CommandSource src, CommandContext args) {
				Player player = (Player) src;
				test(player);
			}
		});
	}

	@Override
	protected void onStop(GameEvent event) {
		unregisterCommand(testCommand);
	}

	private Optional<CommandMapping> registerCommand(final String commandName, String commandDescription, final CommandExecutorWithoutResultThrowsThrowable commandExecutorThrowsThrowable) {
		CommandCallable spec = CommandSpec.builder().description(Texts.of(commandDescription))
			// .arguments(GenericArguments.onlyOne(GenericArguments.world(Texts.of(ARG_WORLD), game)))
			.executor(new CommandExecutor() {
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
					try {
						commandExecutorThrowsThrowable.execute(src, args);
						return CommandResult.success();
					} catch (Throwable e) {
						String msg = commandName + "failed: " + e.getMessage();
						logger.error(msg, e);
						throw new InvocationCommandException(Texts.builder(msg).build(), e);
					}
				}
			})
			.build();

		testCommand = game.getCommandDispatcher().register(plugin, spec, commandName);
		if (!testCommand.isPresent()) {
			logger.error(commandName + " command could not be registered! :-(");
		}
		return testCommand;
	}

	private void unregisterCommand(Optional<CommandMapping> cmd) {
		if (cmd.isPresent()) {
			game.getCommandDispatcher().removeMapping(cmd.get());
		}
	}

}
