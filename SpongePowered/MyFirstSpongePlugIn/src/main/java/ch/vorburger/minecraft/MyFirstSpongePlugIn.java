package ch.vorburger.minecraft;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.block.BlockRedstoneUpdateEvent;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerAboutToStartEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.extra.skylands.SkylandsWorldGeneratorModifier;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "MyFirstSpongePlugIn", name = "My first Sponge Plug-In", version = "1.0")
public class MyFirstSpongePlugIn {

	@Inject Game game;
	@Inject Logger logger;
	@Inject PluginContainer pluginContainer;

	@DefaultConfig(sharedRoot = true)
	@Inject ConfigurationLoader<CommentedConfigurationNode> configLoader;
/*
	@Subscribe
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
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
	@Subscribe
	public void onServerStarting(ServerStartingEvent event) {
		logger.info("hello ServerStartingEvent from MyFirstSpongePlugIn!");
		
		// https://docs.spongepowered.org/en/plugin/basics/commands/creating.html
		// https://github.com/SpongePowered/Cookbook/blob/master/Plugin/WorldEditingTest/src/main/java/org/spongepowered/cookbook/plugin/WorldEditingTest.java
		WorldTeleportCommand worldTeleportCommand = new WorldTeleportCommand();
		CommandCallable tpwCommandSpec = worldTeleportCommand.getCommandSpec(game);
		// TODO put this into a superclass / @Inject helper of WorldTeleportCommand.. 
		Optional<CommandMapping> ok = game.getCommandDispatcher().register(pluginContainer, tpwCommandSpec , "tpw" ,"tpworld");
		if (!ok.isPresent()) {
			logger.error("/tpw Command could not be registered!! :-(");
		}
	}

	@Subscribe
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getUser();
		String name = player.getName();
		logger.info("onPlayerJoin: {} ", name);

		player.sendMessage(Texts.builder("hello! Welcome...").color(TextColors.GOLD).append(Texts.of(name)).build());
		// TODO player.sendTitle(title);

		Location location = player.getLocation();
		World world = player.getWorld();
		Optional<Entity> optional = world.createEntity(EntityTypes.HUMAN, location.getPosition());
		if (optional.isPresent()) {
			Human seymour = (Human) optional.get();
			world.spawnEntity(seymour);
		}
	}

	@Subscribe
	public void onBlockRedstoneUpdateEvent(BlockRedstoneUpdateEvent event) {
		logger.info("onBlockRedstoneUpdateEvent: " + event.getBlock().toString());
	}

	@Subscribe
	public void onPreInit(PreInitializationEvent event) {
		logger.info("hello from MyFirstSpongePlugIn!");
	}

	@Subscribe
	public void onServerStart(ServerStartedEvent event) {
		logger.info("hello ServerStartedEvent from MyFirstSpongePlugIn!");
	}

	@Subscribe
	public void disable(ServerStoppingEvent event) {
		logger.info("bye bye from MyFirstSpongePlugIn!");
	}
}
