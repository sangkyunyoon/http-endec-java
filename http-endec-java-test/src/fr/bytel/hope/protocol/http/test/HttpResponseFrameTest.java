/**
 * 
 */
package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import fr.bmartel.protocol.http.HttpResponseFrame;
import fr.bmartel.protocol.http.HttpVersion;
import fr.bmartel.protocol.http.StatusCodeObject;

/**
 * Http response frame test
 *
 * @author Bertrand Martel
 * 
 */
public class HttpResponseFrameTest {

	@Test
	public void toStringTest() {

		String expectation = "HTTP/1.1 200 OK\r\nheaders1:  value1\r\nheaders2:  value2\r\n\r\n";

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		StatusCodeObject object = new StatusCodeObject(200, "OK");
		HttpVersion version = new HttpVersion(1, 1);

		HttpResponseFrame frame = new HttpResponseFrame(object, version,
				headers, new byte[] {});

		assertTrue("HTTP response frame test",
				frame.toString().equals(expectation));
	}
}
