package ch.vorburger.minecraft.testsinfra;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.translation.FixedTranslation;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.common.text.format.SpongeTextStyle;

import ch.vorburger.minecraft.testsinfra.CommandTestHelper.Chat;
import net.minecraft.util.EnumChatFormatting;

public class CommandTestHelperTest {

	@Test public void textToString() {
		Text text = Texts.of("something");
		assertEquals("something", new CommandTestHelper(null).toString(text));
	}

	@Test public void translatedTextToString() {
		Text text = Texts.builder(new FixedTranslation("something")).build();
		assertEquals("%something:something", new CommandTestHelper(null).toString(text));
	}

	@Test public void chatToString() {
		Chat chat = () -> Collections.singletonList(Texts.of("something"));
		assertEquals("something", new CommandTestHelper(null).toString(chat));
	}

	@Test(expected=AssertionError.class)
	public void assertDoesNotContainLiteralTextFail() {
		Chat chat = () -> Collections.singletonList(Texts.of("something"));
		new CommandTestHelper(null).assertDoesNotContainIgnoreCase(chat, "Something");
	}

	@Test(expected=AssertionError.class)
	public void assertDoesNotContainStyledTextFail() {
		Chat chat = () -> Collections.singletonList(Texts.builder("somethingRed").style(SpongeTextStyle.of(EnumChatFormatting.STRIKETHROUGH)).build());
		new CommandTestHelper(null).assertDoesNotContainIgnoreCase(chat, "something");
	}

	@Test(expected=AssertionError.class)
	public void assertDoesNotContainTranslationFail() {
		Chat chat = () -> Collections.singletonList(Texts.builder(new FixedTranslation("something")).build());
		new CommandTestHelper(null).assertDoesNotContainIgnoreCase(chat, "something");
	}

	@Test public void assertDoesNotContainPass() {
		Chat chat = () -> Collections.singletonList(Texts.of("anything"));
		new CommandTestHelper(null).assertDoesNotContainIgnoreCase(chat, "something");
	}

	@Test(expected=AssertionError.class) public void assertSuccessCountFail() {
		new CommandTestHelper(null).assertSuccessCount(CommandResult.empty());
	}

	@Test public void assertSuccessCountPass() {
		new CommandTestHelper(null).assertSuccessCount(CommandResult.success());
	}

}
