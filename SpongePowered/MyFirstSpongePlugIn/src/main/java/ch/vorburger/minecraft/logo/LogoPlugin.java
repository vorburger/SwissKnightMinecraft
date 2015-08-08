package ch.vorburger.minecraft.logo;

import java.util.Map;
import java.util.function.Function;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.common.collect.MapMaker;
import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;
import ch.vorburger.minecraft.command.Command;
import ch.vorburger.minecraft.command.CommandManager;

@Plugin(id = "LogoPlugin", name = "Logo-like commands (thank you, Seymour Papert)", version = "1.0")
public class LogoPlugin extends AbstractHotPlugin {

	@Inject PluginContainer plugin;
	@Inject CommandManager commandManager;

	Map<Player, Turtle> playerTurtleMap = new MapMaker().makeMap();
	
	@Command("draw big cube")
	public void cube(Player player) {
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
	public void fd(Player player) {
		getTurtle(player).moveForward();
	}

	@Command("Move Turtle backward, and draw if pen down")
	public void bk(Player player) {
		getTurtle(player).moveBack();
	}

	@Command("Move Turtle upward, and draw if pen down")
	public void up(Player player) {
		getTurtle(player).moveUp();
	}

	@Command("Move Turtle downward, and draw if pen down")
	public void dn(Player player) {
		getTurtle(player).moveDown();
	}

	@Command("Turn Turtle right (no drawing)")
	public void rt(Player player) {
		getTurtle(player).turnRight();
	}

	@Command("Turn Turtle left (no drawing)")
	public void lt(Player player) {
		getTurtle(player).turnLeft();
	}

	@Command("Raise Turtle's Pen up")
	public void pu(Player player) {
		getTurtle(player).noSetBlockOnMove();
	}

	@Command("Put Turtle's Pen down")
	public void pd(Player player) {
		getTurtle(player).setBlockOnMove();
	}

	@Command("Make Turtle remove block")
	public void rm(Player player) {
		getTurtle(player).remove();
	}

	@Command("Make Turtle interact")
	public void inter(Player player) {
		getTurtle(player).interact();
	}

	@Command("Make Turtle dig")
	public void dig(Player player) {
		getTurtle(player).dig();
	}
	
	protected Turtle getTurtle(final Player player) {
		return playerTurtleMap.computeIfAbsent(player, new Function<Player, Turtle>() {
			public Turtle apply(Player t) {
				return new Turtle(player);
			}
		});
	}
	
	@Override
	protected void onLoaded(GameEvent event) {
		commandManager.register(plugin, this);
	}

	@Override
	protected void onStop(GameEvent event) {
		commandManager.unregister();
	}

}
