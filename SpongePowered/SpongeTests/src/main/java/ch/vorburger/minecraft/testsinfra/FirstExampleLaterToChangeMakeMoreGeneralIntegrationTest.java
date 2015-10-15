package ch.vorburger.minecraft.testsinfra;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongepowered.api.Game;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

// TODO JavaDoc @author Michael Vorburger
@RunWith(MinecraftRunner.class)
public class FirstExampleLaterToChangeMakeMoreGeneralIntegrationTest {
	// TODO after re-factoring, rename simply to AliasPluginTest

	// private static final Logger logger = LoggerFactory.getLogger(FirstExampleLaterToChangeMakeMoreGeneralIntegrationTest.class);

	// TODO @Inject
	public Game game;

	@Test public void testAliasCommandNoArguments() throws Throwable {
		// TODO factor this out into a CommandTestHelper
		CommandService commandService = game.getCommandDispatcher();
		CommandSource source = (CommandSource) game.getServer();
		CommandResult result = commandService.process(source, "alias");
		// TODO assert on result to make sure it was successful.. propagate error if not
	}

}
