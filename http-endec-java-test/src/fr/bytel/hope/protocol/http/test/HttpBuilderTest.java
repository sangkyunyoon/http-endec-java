package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.junit.Test;

import fr.bmartel.protocol.http.HttpBuilder;
import fr.bmartel.protocol.http.constants.StatusCodeList;
import fr.bytel.hope.protocol.http.mock.ListOfBytesMock;

/**
 * Http builder test class
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpBuilderTest {

	@Test
	public void httpRequestTest() {
		/* post request */
		String expectation = "POST /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\n";

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		try {
			assertTrue(
					"HTTP request frame test POST",
					HttpBuilder.httpRequest("POST", "/rest/help/todo",
							new ListOfBytesMock("bodyTobeWritten"), headers).equals(expectation));
		} catch (UnsupportedEncodingException e) {
			fail("UnsupportedEncodingException");
		}
		/* get request */
		expectation = "GET /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\n";

		headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		try {
			assertTrue(
					"HTTP request frame test GET",
					HttpBuilder.httpRequest("GET", "/rest/help/todo",
							new ListOfBytesMock("bodyTobeWritten"), headers).equals(expectation));
		} catch (UnsupportedEncodingException e) {
			fail("UnsupportedEncodingException");
		}

		/* http response OK test */
		expectation = "200 OK HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\n\r\n\r\n";

		headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		try {
			assertTrue(
					"HTTP request frame test GET",
					HttpBuilder.httpRequest("", StatusCodeList.OK.toString(),
							new ListOfBytesMock(""), headers).equals(
							expectation));
		} catch (UnsupportedEncodingException e) {
			fail("UnsupportedEncodingException");
		}
	}
}
