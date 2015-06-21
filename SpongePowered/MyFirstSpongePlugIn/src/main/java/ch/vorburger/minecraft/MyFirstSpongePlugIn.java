package ch.vorburger.minecraft;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.monster.Creeper;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.block.BlockRedstoneUpdateEvent;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "MyFirstSpongePlugIn", name = "My first Sponge Plug-In", version = "1.0")
public class MyFirstSpongePlugIn {

	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;

	@Inject
	private Game game;

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
