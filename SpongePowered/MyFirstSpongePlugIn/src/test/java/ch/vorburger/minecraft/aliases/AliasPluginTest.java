package ch.vorburger.minecraft.aliases;

//import ch.vorburger.minecraft.testsinfra.CommandTestHelper;
//import ch.vorburger.minecraft.testsinfra.CommandTestHelper.CommandResultWithChat;
//import ch.vorburger.minecraft.testsinfra.MinecraftRunner;

/**
 * Integration Tests for AliasPlugin.
 *
 * @author Michael Vorburger
 */
//@RunWith(MinecraftRunner.class)
public class AliasPluginTest {
	/*
	public Game game;
	CommandTestHelper h;

	@Before public void before() {
		h = new CommandTestHelper(game);
	}

	// This is intentionally one single long test, because MinecraftRunner
	// cannot yet cleanly reset everything for every test, so if instead these
	// were separate small @Test methods there could be inter test dependency
	// and ordering problems etc.

	@Test public void testAliasCommand() throws Throwable {
		// Initially aliases listing should be empty
		assertNoChatReply("def");

		// Create a simple single command alias, and test it
		assertNoChatReply("def helloworld /say Hello World");
		// TODO	assertSingleChatReply("helloworld", "says Hello World");

		// Now the aliases listing should have it
		h.assertSingleChatReply("def", "helloworld");

		// We can view it
		// TODO	assertSingleChatReply("def helloworld", "def helloworld /me says Hello World");

		// Create a more interesting multi command
		assertNoChatReply("def helloworld /me says Hello World /say Gruezi");
		assertTwoChatReplies("helloworld", "WHO? says Hello World", "Gruezi");

		// We can delete it
		h.assertSingleChatReply("def helloworld /", "helloworld deleted");

		// Now aliases listing should be empty again
		assertNoChatReply("def");
	}


	// TODO factor these out into a helper
	// TODO I'm missing Hamcrest.. these should be matchers?

	private void assertNoChatReply(String command) {
		assertEquals(0, h.process(command).getChat().getMessages().size());
	}

	private void assertTwoChatReplies(String command, String firstExpectedChatReply, String secondExpectedChatReply) {
		CommandResultWithChat r = h.process(command);
		List<Text> m = r.getChat().getMessages();
		assertEquals(2, m.size());
		assertEquals(firstExpectedChatReply, h.toString(m.get(0)));
		assertEquals(secondExpectedChatReply, h.toString(m.get(1)));
	}

	 */
}
