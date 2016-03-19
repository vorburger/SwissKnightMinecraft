package ch.vorburger.minecraft;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class WorldTeleportCommand implements CommandExecutor {
	// https://github.com/SpongePowered/Cookbook/blob/master/Plugin/WorldsTest/src/main/java/org/spongepowered/cookbook/plugin/WorldsTest.java

	private static final String ARG_WORLD = "world";

	// TOOD @Inject Game game instead of argument below (and have it create by
	// Guice in *Plugin)

	CommandSpec getCommandSpec(Game game) {
		return CommandSpec.builder().description(Text.of("Teleport to another world"))
				// .permission("myplugin.command.helloworld")
				.arguments(GenericArguments.onlyOne(GenericArguments.world(Text.of(ARG_WORLD)))).executor(this)
				.build();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player player = (Player) src;

		World world = args.<World> getOne(ARG_WORLD).get();

		Vector3d spawnPosition = world.getProperties().getSpawnPosition().toDouble();
		player.transferToWorld(world.getName(), spawnPosition);

		return CommandResult.success();
	}

}
