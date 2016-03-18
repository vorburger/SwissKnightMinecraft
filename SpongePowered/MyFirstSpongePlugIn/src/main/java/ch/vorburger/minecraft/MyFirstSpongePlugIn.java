package ch.vorburger.minecraft;

import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandMapping;

import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;
import ch.vorburger.minecraft.utils.SpawnHelper;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

// @Plugin(id = "MyFirstSponge", name = "My first Sponge Plug-In", version = "1.0")
public class MyFirstSpongePlugIn extends AbstractHotPlugin {

	@Inject Game game;
	@Inject Logger logger;
	@Inject PluginContainer plugin;

	@DefaultConfig(sharedRoot = true)
	@Inject ConfigurationLoader<CommentedConfigurationNode> configLoader;

	Optional<CommandMapping> commandMapping = Optional.empty();

	SpawnHelper spawnHelper = new SpawnHelper();

	/*  https://github.com/SpongePowered/SpongeVanilla/issues/175
	 *
	@Subscribe
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
    	// https://github.com/SpongePowered/Cookbook/blob/master/Plugin/WorldsTest/src/main/java/org/spongepowered/cookbook/plugin/WorldsTest.java
		final SkylandsWorldGeneratorModifier skylandsModifier = new SkylandsWorldGeneratorModifier();
        this.game.getRegistry().registerWorldGeneratorModifier(skylandsModifier);


        this.game.getRegistry().getWorldBuilder()
        	.name("skylands")
	        .enabled(true)
	        .loadsOnStartup(true)
	        .keepsSpawnLoaded(true)
	        .dimensionType(DimensionTypes.OVERWORLD)
	        .generator(GeneratorTypes.OVERWORLD)
	        .generatorModifiers(skylandsModifier)
	        .gameMode(GameModes.CREATIVE)
        .build();
	}
	 */

	@Override
	protected void onLoaded(GameEvent event) {
		logger.info("hello ServerStartingEvent from MyFirstSpongePlugIn!");

		// https://docs.spongepowered.org/en/plugin/basics/commands/creating.html
		// https://github.com/SpongePowered/Cookbook/blob/master/Plugin/WorldEditingTest/src/main/java/org/spongepowered/cookbook/plugin/WorldEditingTest.java
		WorldTeleportCommand worldTeleportCommand = new WorldTeleportCommand();
		CommandCallable tpwCommandSpec = worldTeleportCommand.getCommandSpec(game);
		// TODO put this into a superclass / @Inject helper of WorldTeleportCommand..
		commandMapping = game.getCommandDispatcher().register(plugin, tpwCommandSpec , "tpw" ,"tpworld");
		if (!commandMapping.isPresent()) {
			logger.error("/tpw Command could not be registered!! :-(");
		}
	}

	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event) {
		Player player = event.getTargetEntity();
		String name = player.getName();
		logger.info("onPlayerJoin: {} ", name);

		player.sendMessage(Texts.builder("hello! Welcome...").color(TextColors.GOLD).append(Texts.of(name)).build());
		// TODO player.sendTitle(title);

		// /* Optional<Human> seymour = */ spawnHelper.spawnLNE(Human.class, player.getLocation());
	}

	@Override
	protected void onStop(GameEvent event) {
		logger.info("bye bye from MyFirstSpongePlugIn!");

		if (commandMapping.isPresent()) {
			game.getCommandDispatcher().removeMapping(commandMapping.get());
		}
	}
}
