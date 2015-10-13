package ch.vorburger.minecraft.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.flowpowered.math.vector.Vector3i;

public class IdentifiableHelperTest {

	@Test public void testIDParts() {
		IdentifiableHelper h = new IdentifiableHelper(null);
		String id = h.newID("scheme", "part1", '.', "part2");
		String[] parts = h.fromID(id);
		assertEquals("scheme", parts[0]);
		assertEquals("part1", parts[1]);
		assertEquals("part2", parts[2]);
	}

	@Test public void testVector3iToFromString() {
		IdentifiableHelper h = new IdentifiableHelper(null);
		Vector3i v = new Vector3i(1, 2, 3);
		assertEquals(v, h.parseVector3i(h.toString(v)));
	}
}
