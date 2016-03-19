package ch.vorburger.minecraft.command;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.spongepowered.api.Game;

//import ch.vorburger.minecraft.testsinfra.CommandTestHelper;
//import ch.vorburger.minecraft.testsinfra.MinecraftRunner;

@Ignore
//@RunWith(MinecraftRunner.class)
public class CommandTestHelperTest {

	public  Game game;
	//private CommandTestHelper h;

	@Before public void before() {
		//h = new CommandTestHelper(game);
	}

	@Test public void testBadCommandNameShouldFail() throws Throwable {
		try {
			//h.process("badCommand");
			fail("This should have failed");
		} catch (AssertionError e) {
			assertTrue(e.getMessage(), e.getMessage().contains("commands.generic.notFound"));
		}
	}

	@Test public void testCommandTestHelperSingleChatLine() throws Throwable {
		//h.assertSingleChatReply("sayself Hello World", "Hello World");
	}

}
