package ch.vorburger.minecraft.learning;


import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.player;
import static org.spongepowered.api.command.args.GenericArguments.seq;

import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.Cause.Builder;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.EntityUniverse;

@Plugin(id = "ch.vorburger.minecraft.learning", name = "Learning Plugin", description = "Learn things while playing in Minecraft", version = "1.0", url = "http://vorburger.ch", authors = { "vorburger" })
public class LearningPlugin {

	@Inject Logger logger;
	@Inject PluginContainer plugin;

	Optional<CommandMapping> commandMapping = Optional.empty();
	static @Nullable Human staticProfessor;

	@Listener
    public void onServerStart(GameStartedServerEvent event) {
		logger.info("hey there!");
    }
	
	@Listener
	public void onServerStarting(GameStartingServerEvent event) {
		logger.info("hello ServerStartingEvent from MyFirstSpongePlugIn!");

		CommandSpec commandSpec = CommandSpec.builder()
				.arguments(optional(seq(player(Text.of("player")))))
			    .description(Text.of("Question!"))
//			    .permission("ch.vorburger.minecraft.learning.question")
			    .executor(new QuestionCommand())
			    .build();
		commandMapping = Sponge.getCommandManager().register(plugin, commandSpec , "question");
		if (!commandMapping.isPresent()) {
			logger.error("Command could not be registered!! :-(");
		}
	}

	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event) {
		Player player = event.getTargetEntity();
		String name = player.getName();
		logger.info("onPlayerJoin: {} ", name);

		// Human professor = spawnHuman(player);
		// TODO urgh! Fix this later...
		//staticProfessor  = professor; 
		
		// TODO welcome BACK - long time no see since lastPlayed()
		LiteralText text = Text.builder("Welcome ").color(TextColors.GOLD).append(Text.of(name))
				.color(TextColors.DARK_GREEN).append(Text.of("! Try /question <friend>")).build();
		player.sendMessage(text);
		
		Title title = Title.builder().fadeIn(40).fadeOut(80).title(Text.builder("Welcome back!").build()).build();
		player.sendTitle(title);
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event) {
		if (commandMapping.isPresent()) {
			Sponge.getCommandManager().removeMapping(commandMapping.get());
		}
	}
	
	private Human spawnHuman(Entity causeAndLocation) {
		return (Human) spawn(EntityTypes.HUMAN, causeAndLocation).get();
	}
	
	private Optional<Entity> spawn(EntityType entityType, Entity causeAndLocation) {
		Location<World> location = causeAndLocation.getLocation();
		EntityUniverse entityUniverse = location.getExtent();
		Optional<Entity> optionalEntity = entityUniverse.createEntity(entityType, location.getPosition());
		if (optionalEntity.isPresent()) {
			Entity newEntity = optionalEntity.get();
			Cause cause = Cause.source(EntitySpawnCause.builder().entity(causeAndLocation).type(SpawnTypes.PLUGIN).build()).build();
			boolean isSpawned = entityUniverse.spawnEntity(newEntity, cause);
			if (!isSpawned) {
				logger.error("Could not spawn new Entity: " + entityType.getName());
				return Optional.empty();
			}
			return Optional.of(newEntity);
		} else {
			logger.error("Could not create new Entity: " + entityType.getName());
			return Optional.empty();
		}
	}
}
