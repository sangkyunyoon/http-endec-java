package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.bmartel.protocol.http.HttpVersion;


/**
 * Test class for Http version class
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpVersionTest {

	@Test
	public void httpVersionTest() throws Exception {
		String httpVersion = "HTTP/1.1 200 OK";
		assertTrue("HTTP VERSION test 1 digit",
				(new HttpVersion(httpVersion)).versionDigit1 == 1);
		assertTrue("HTTP VERSION test 2 digit",
				(new HttpVersion(httpVersion)).versionDigit2 == 1);

		httpVersion = "HTTP/2.0 200 NOT FOUND";
		assertTrue("HTTP VERSION test 1 digit",
				(new HttpVersion(httpVersion)).versionDigit1 == 2);
		assertTrue("HTTP VERSION test 2 digit",
				(new HttpVersion(httpVersion)).versionDigit2 == 0);

	}

	@Test
	public final void testToString() {
		String httpVersion = "HTTP/1.1";
		assertTrue("HTTP VERSION test HTTP/1.1 digit", (new HttpVersion(1, 1))
				.toString().equals(httpVersion));

		httpVersion = "HTTP/2.1";
		assertTrue("HTTP VERSION test HTTP/2.1 digit", (new HttpVersion(2, 1))
				.toString().equals(httpVersion));
	}
}
