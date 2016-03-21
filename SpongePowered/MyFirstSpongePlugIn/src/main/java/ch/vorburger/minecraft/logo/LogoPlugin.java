package ch.vorburger.minecraft.logo;

import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.command.source.LocatedSource;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.plugin.Plugin;

import com.google.common.collect.MapMaker;

import ch.vorburger.minecraft.command.AbstractPluginWithCommands;
import ch.vorburger.minecraft.command.Command;
import ch.vorburger.minecraft.utils.MinecraftHelperException;
import ch.vorburger.minecraft.utils.SpawnHelper;

@Plugin(id = "ch.vorburger.minecraft.logo", name = "LOGO", description="Logo-like commands (thank you, Seymour Papert)", version = "1.0")
public class LogoPlugin extends AbstractPluginWithCommands {
	private final static Logger logger = LoggerFactory.getLogger(LogoPlugin.class);

	Map<LocatedSource, TurtleImpl> playerTurtleMap = new MapMaker().makeMap();
	SpawnHelper spawnHelper = new SpawnHelper();

	@Command("draw big cube")
	public void cube(LocatedSource player) {
		TurtleImpl turtle = new TurtleImpl(player);
		for (int h = 0; h < 3; h++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 5; j++) {
					turtle.fwd();
				}
				turtle.rt();
			}
			turtle.up();
		}
		// TODO For Dév, fill everything inside the house with Air.. eval. using /fill command!
	}

	// TODO New Command to change Block, pick it from Player
	// TODO New Command to create a new turtle for the Player


	@Command("Move Turtle forward, and draw if pen down")
	public void fd(LocatedSource player) {
		getTurtle(player).fwd();
	}

	@Command("Move Turtle backward, and draw if pen down")
	public void bk(LocatedSource player) {
		getTurtle(player).back();
	}

	@Command("Move Turtle upward, and draw if pen down")
	public void up(LocatedSource player) {
		getTurtle(player).up();
	}

	@Command("Move Turtle downward, and draw if pen down")
	public void dn(LocatedSource player) {
		getTurtle(player).down();
	}

	@Command("Turn Turtle right (no drawing)")
	public void rt(LocatedSource player) {
		getTurtle(player).rt();
	}

	@Command("Turn Turtle left (no drawing)")
	public void lt(LocatedSource player) {
		getTurtle(player).lt();
	}

	@Command("Raise Turtle's Pen up")
	public void pu(LocatedSource player) {
		getTurtle(player).noSetBlockOnMove();
	}

	@Command("Put Turtle's Pen down")
	public void pd(LocatedSource player) {
		getTurtle(player).setBlockOnMove();
	}

	@Command("Make Turtle remove block")
	public void rm(LocatedSource player) {
		getTurtle(player).remove();
	}

	//	@Command("Make Turtle interact")
	//	public void inter(Player player) {
	//		getTurtle(player).interact();
	//	}

	//	@Command("Make Turtle dig")
	//	public void dig(LocatedSource player) {
	//		getTurtle(player).dig();
	//	}

	protected TurtleImpl getTurtle(LocatedSource source) {
		return playerTurtleMap.computeIfAbsent(source, new Function<LocatedSource, TurtleImpl>() {
			@Override
			public TurtleImpl apply(LocatedSource t) {
				try {
					Human turtleEntity = spawnHelper.spawn(Human.class, t.getLocation());

					// turtleEntity.offer(Keys.CAN_FLY, true);
					// turtleEntity.offer(Keys.IS_FLYING, true);
					// turtleEntity.offer(Keys.FLYING_SPEED, 0.0);

					logger.info("Couldn't find Seymour Human Companion, so spawned a new one at: " + turtleEntity.getLocation().toString());
					return new EntityConnectedTurtle(turtleEntity, t);
				} catch (MinecraftHelperException e) {
					logger.error("Unable to spawn Seymour Human Turtle for LocatedSource, falling back to invisible Turtle: " + source, e);
					return new TurtleImpl(source);
				}

			}
		});
	}

}
