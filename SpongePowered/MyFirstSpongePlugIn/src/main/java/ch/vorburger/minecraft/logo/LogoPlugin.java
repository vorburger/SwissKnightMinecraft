package ch.vorburger.minecraft.logo;

import java.util.Map;
import java.util.function.Function;

import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.command.source.LocatedSource;

import com.google.common.collect.MapMaker;

import ch.vorburger.minecraft.command.AbstractHotPluginWithCommands;
import ch.vorburger.minecraft.command.Command;
import ch.vorburger.minecraft.utils.MinecraftHelperException;
import ch.vorburger.minecraft.utils.SpawnHelper;

@Plugin(id = "Logo", name = "Logo-like commands (thank you, Seymour Papert)", version = "1.0")
public class LogoPlugin extends AbstractHotPluginWithCommands {

	Map<LocatedSource, Turtle> playerTurtleMap = new MapMaker().makeMap();
	SpawnHelper spawnHelper = new SpawnHelper();
	
	@Command("draw big cube")
	public void cube(LocatedSource player) {
		Turtle turtle = new Turtle(player);
		for (int h = 0; h < 3; h++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 5; j++) {
					turtle.moveForward();
				}
				turtle.turnRight();
			}
			turtle.moveUp();
		}
		// TODO For Dév, fill everything inside the house with Air.. eval. using /fill command!
	}

	// TODO New Command to change Block, pick it from Player
	// TODO New Command to create a new turtle for the Player
	

	@Command("Move Turtle forward, and draw if pen down")
	public void fd(LocatedSource player) {
		getTurtle(player).moveForward();
	}

	@Command("Move Turtle backward, and draw if pen down")
	public void bk(LocatedSource player) {
		getTurtle(player).moveBack();
	}

	@Command("Move Turtle upward, and draw if pen down")
	public void up(LocatedSource player) {
		getTurtle(player).moveUp();
	}

	@Command("Move Turtle downward, and draw if pen down")
	public void dn(LocatedSource player) {
		getTurtle(player).moveDown();
	}

	@Command("Turn Turtle right (no drawing)")
	public void rt(LocatedSource player) {
		getTurtle(player).turnRight();
	}

	@Command("Turn Turtle left (no drawing)")
	public void lt(LocatedSource player) {
		getTurtle(player).turnLeft();
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

	@Command("Make Turtle dig")
	public void dig(LocatedSource player) {
		getTurtle(player).dig();
	}
	
	protected Turtle getTurtle(LocatedSource player) {
		return playerTurtleMap.computeIfAbsent(player, new Function<LocatedSource, Turtle>() {
			public Turtle apply(LocatedSource t) {
				try {
					Human turtleEntity = spawnHelper.spawn(Human.class, t);
					
					// TODO https://docs.spongepowered.org/en-GB/plugin/data/index.html
					// TODO Move this to a new DataHelper utility class? And an additional EntityHelper with simply fly(Entity) method?
					// ??? turtleEntity.getData(FlyingData.class).get().set(true);
					
					return new EntityConnectedTurtle(turtleEntity);
				} catch (MinecraftHelperException e) {
					return new Turtle(player);
				}
				
			}
		});
	}

}
