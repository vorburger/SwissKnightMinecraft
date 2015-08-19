package ch.vorburger.minecraft.command;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

import ch.vorburger.minecraft.command.CommandManager.MethodArg;

public class CommandManagerTest {

	@Command void alias(CommandSource commandSource, Optional<String> name, Optional<String> commandsToAlias) { };

	@Test public void getCommandElements() throws Exception {
		Method method = this.getClass().getDeclaredMethod("alias", CommandSource.class, Optional.class, Optional.class);
		List<MethodArg> methodArgs = new CommandManager().getMethodArgs(method);
		assertEquals(3, methodArgs.size());
		
		assertEquals("commandSource", methodArgs.get(0).name);
		assertFalse(methodArgs.get(0).optional);
		assertEquals(CommandSource.class, methodArgs.get(0).type);		
		
		assertEquals("name", methodArgs.get(1).name);
		assertTrue(methodArgs.get(1).optional);
		assertEquals(String.class, methodArgs.get(1).type);		

		assertEquals("commandsToAlias", methodArgs.get(2).name);
		assertTrue(methodArgs.get(2).optional);
		assertEquals(String.class, methodArgs.get(2).type);		
	}
}
