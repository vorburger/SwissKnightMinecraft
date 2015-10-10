package ch.vorburger.minecraft.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.spongepowered.api.util.command.CommandSource;

import ch.vorburger.minecraft.command.CommandManager.MethodArg;

public class CommandManagerTest {

	@Command void test(CommandSource commandSource, Optional<String> name, int n, String commandToRepeat) { };

	@Test public void getCommandElements() throws Exception {
		Method method = this.getClass().getDeclaredMethod("test", CommandSource.class, Optional.class, Integer.TYPE, String.class);
		List<MethodArg> methodArgs = new CommandManager().getMethodArgs(method);
		assertEquals(4, methodArgs.size());
		
		assertEquals("commandSource", methodArgs.get(0).name);
		assertFalse(methodArgs.get(0).optional);
		assertEquals(CommandSource.class, methodArgs.get(0).type);		
		
		assertEquals("name", methodArgs.get(1).name);
		assertTrue(methodArgs.get(1).optional);
		assertEquals(String.class, methodArgs.get(1).type);		

		assertEquals("n", methodArgs.get(2).name);
		assertFalse(methodArgs.get(2).optional);
		assertEquals(Integer.TYPE, methodArgs.get(2).type);
		
		assertEquals("commandToRepeat", methodArgs.get(3).name);
		assertFalse(methodArgs.get(3).optional);
		assertEquals(String.class, methodArgs.get(3).type);		

		// TODO test vararg, array, and list
	}
}
