package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.bmartel.protocol.http.constants.StatusCodeList;

/**
 * @author Bertrand Martel
 * 
 */
public class StatusCodeListTest {

	@Test
	public void continueTest() {
		assertTrue("CONTINUE status code", StatusCodeList.CONTINUE.code == 100);
		assertTrue("CONTINUE status value",
				StatusCodeList.CONTINUE.reasonPhrase.equals("Continue"));
	}
}
