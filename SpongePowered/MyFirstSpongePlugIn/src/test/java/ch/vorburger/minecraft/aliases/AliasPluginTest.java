package ch.vorburger.minecraft.aliases;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongepowered.api.Game;

import ch.vorburger.minecraft.testsinfra.CommandTestHelper;
import ch.vorburger.minecraft.testsinfra.MinecraftRunner;

/**
 * Integration Tests for AliasPlugin.
 *
 * @author Michael Vorburger
 */
@RunWith(MinecraftRunner.class)
public class AliasPluginTest {

	public Game game;

	@Test public void testAliasCommandNoArguments() throws Throwable {
		CommandTestHelper h = new CommandTestHelper(game);
		h.process("alias");
	}

}
