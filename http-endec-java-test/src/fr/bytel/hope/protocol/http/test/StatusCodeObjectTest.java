package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.bmartel.protocol.http.StatusCodeObject;

/**
 * Status code object test class
 * 
 * @author Bertrand Martel
 * 
 */
public class StatusCodeObjectTest {

	@Test
	public final void testToString() {
		String test = 200 + " " + "OK";
		assertTrue("200 OK", (new StatusCodeObject(200, "OK")).toString()
				.equals(test));
	}
}
