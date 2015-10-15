/*
 * This file is part of Michael Vorburger's SwissKnightMinecraft project, licensed under the MIT License (MIT).
 *
 * Copyright (c) Michael Vorburger <http://www.vorburger.ch>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.vorburger.minecraft.testsinfra;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.spongepowered.api.Game;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Literal;
import org.spongepowered.api.text.Text.Translatable;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

/**
 * Helper for integration tests using Minecraft slash commands.
 *
 * @see CommandService
 *
 * @author Michael Vorburger
 */
public class CommandTestHelper {

	protected Game game;

	// TODO remove constructor, use @Inject Game game instead
	public CommandTestHelper(Game game) {
		this.game = game;
	}

	public CommandResultWithChat process(String command) {
		CommandSource source = (CommandSource) game.getServer();
		return process(source, command);
	}

	public CommandResultWithChat process(CommandSource source, String command) {
		ChatKeepingCommandSource wrappedSource = wrap(source);
		CommandService commandService = game.getCommandDispatcher();
		CommandResult result = commandService.process(wrappedSource, command);
		// TODO Ideally make this more robust.. would require changes to core Sponge
		assertDoesNotContainIgnoreCase(wrappedSource, "commands.generic.notFound"); // "Unknown command"
		assertDoesNotContainIgnoreCase(wrappedSource, "Error occurred while executing command"); // as in SimpleCommandService
		return new CommandResultWithChat(result, wrappedSource);
	}

	protected String toString(Text text) {
		StringBuilder sb = new StringBuilder();
		for (Text childText : text.withChildren()) {
			if (sb.length() > 0)
				sb.append("; ");
			if (childText instanceof Literal) {
				sb.append(((Literal)childText).getContent());
			} else if (childText instanceof Translatable) {
				Translation translation = ((Translatable)childText).getTranslation();
				// We're opt. also adding Translation here Id because due to a
				// strange problem get() doesn't always seem to work (weird
				// MissingResourceException when called from here; but works
				// normally elsewhere - more ClassLoader
				// shit??)
				if (!translation.getId().equals(translation.get(Locale.US))) {
					sb.append('%');
					sb.append(translation.getId());
					sb.append(": ");
				}
				sb.append(translation.get(Locale.US));
			} else {
				// TODO Other sub classes of Text...
				throw new IllegalArgumentException(childText.getClass().getName());
			}
		}
		return sb.toString();
	}

	protected String toString(Chat chat) {
		StringBuilder sb = new StringBuilder();
		for (Text text : chat.getMessages()) {
			if (sb.length() > 0)
				sb.append('\n');
			sb.append(toString(text));
		}
		return sb.toString();
	}

	public boolean contains(Chat chat, String text) {
		return toString(chat).contains(text);
	}

	public boolean containsIgnoreCase(Chat chat, String text) {
		return toString(chat).toLowerCase().contains(text.toLowerCase());
	}

	public void assertDoesNotContainIgnoreCase(Chat chat, String text) throws AssertionError {
		Assert.assertFalse(toString(chat), containsIgnoreCase(chat, text));
	}

	public void assertContainsIgnoreCase(Chat chat, String text) throws AssertionError {
		Assert.assertTrue(toString(chat), containsIgnoreCase(chat, text));
	}

	public void assertContainsNoErrors(Chat chat) throws AssertionError {
		assertDoesNotContainIgnoreCase(chat, "error");
	}

	public void assertSuccessCount(CommandResult result) throws AssertionError {
		if (!result.getSuccessCount().isPresent())
			throw new AssertionError("CommandResult has no (empty) successCount");
		int successCount = result.getSuccessCount().get().intValue();
		if (successCount < 1)
			throw new AssertionError("CommandResult had successCount != 1: " + successCount);
	}

	public /*static*/ class CommandResultWithChat {
		private final CommandResult result;
		private final Chat chat;
		protected CommandResultWithChat(CommandResult result, Chat chat) {
			this.result = result;
			this.chat = chat;
		}
		public CommandResult getResult() {
			return result;
		}
		public Chat getChat() {
			return chat;
		}
		public CommandResultWithChat assertSuccessCount() throws AssertionError {
			CommandTestHelper.this.assertSuccessCount(this.result);
			return this;
		}
		public CommandResultWithChat assertChatNoErrors() throws AssertionError {
			CommandTestHelper.this.assertContainsNoErrors(this.chat);
			return this;
		}
	}

	public static interface Chat {
		List<Text> getMessages();
	}

	protected ChatKeepingCommandSource wrap(CommandSource source) {
		return new ChatKeepingCommandSource(source);
	}

	protected static class ChatKeepingCommandSource extends DelegatingCommandSource implements Chat {
		private List<Text> sentMessages = new ArrayList<Text>();

		public ChatKeepingCommandSource(CommandSource source) {
			super(source);
		}

		@Override
		public List<Text> getMessages() {
			return sentMessages;
		}

		@Override
		public void sendMessage(Iterable<Text> messages) {
			for (Text message : messages) {
				sentMessages.add(message);
			}
			super.sendMessage(messages);
		}

		@Override
		public void sendMessage(Text... messages) {
			for (Text message : messages) {
				sentMessages.add(message);
			}
			super.sendMessage(messages);
		}
	}

	protected static class DelegatingCommandSource implements CommandSource {
		protected CommandSource delegate;

		public DelegatingCommandSource(CommandSource source) {
			this.delegate = source;
		}

		@Override
		public String getName() {
			return delegate.getName();
		}

		@Override
		public void sendMessage(Text... messages) {
			delegate.sendMessage(messages);
		}

		@Override
		public void sendMessage(Iterable<Text> messages) {
			delegate.sendMessage(messages);
		}

		@Override
		public MessageSink getMessageSink() {
			return delegate.getMessageSink();
		}

		@Override
		public void setMessageSink(MessageSink sink) {
			delegate.setMessageSink(sink);
		}

		@Override
		public String getIdentifier() {
			return delegate.getIdentifier();
		}

		@Override
		public Optional<CommandSource> getCommandSource() {
			return delegate.getCommandSource();
		}

		@Override
		public SubjectCollection getContainingCollection() {
			return delegate.getContainingCollection();
		}

		@Override
		public SubjectData getSubjectData() {
			return delegate.getSubjectData();
		}

		@Override
		public SubjectData getTransientSubjectData() {
			return delegate.getTransientSubjectData();
		}

		@Override
		public boolean hasPermission(Set<Context> contexts, String permission) {
			return delegate.hasPermission(contexts, permission);
		}

		@Override
		public boolean hasPermission(String permission) {
			return delegate.hasPermission(permission);
		}

		@Override
		public Tristate getPermissionValue(Set<Context> contexts, String permission) {
			return delegate.getPermissionValue(contexts, permission);
		}

		@Override
		public boolean isChildOf(Subject parent) {
			return delegate.isChildOf(parent);
		}

		@Override
		public boolean isChildOf(Set<Context> contexts, Subject parent) {
			return delegate.isChildOf(contexts, parent);
		}

		@Override
		public List<Subject> getParents() {
			return delegate.getParents();
		}

		@Override
		public List<Subject> getParents(Set<Context> contexts) {
			return delegate.getParents(contexts);
		}

		@Override
		public Set<Context> getActiveContexts() {
			return delegate.getActiveContexts();
		}

	}

}
