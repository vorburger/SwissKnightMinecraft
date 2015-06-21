package ch.vorburger.minecraft;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class WorldTeleportCommand implements CommandExecutor {

	private static final String ARG_WORLD = "world";

	// TOOD @Inject Game game instead of argument below (and have it create by
	// Guice in *Plugin)

	CommandSpec getCommandSpec(Game game) {
		return CommandSpec.builder().description(Texts.of("Teleport to another world"))
				// .permission("myplugin.command.helloworld")
				.arguments(GenericArguments.onlyOne(GenericArguments.world(Texts.of(ARG_WORLD), game))).executor(this)
				.build();
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player player = (Player) src;
		
		World world = args.<World> getOne(ARG_WORLD).get();
		
		Vector3d spanwPosition = new Vector3d();
		player.transferToWorld(world.getName(), spanwPosition);
		
		return CommandResult.success();
	}

}
