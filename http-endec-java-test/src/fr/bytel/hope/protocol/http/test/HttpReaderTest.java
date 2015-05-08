package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import fr.bmartel.protocol.http.HttpReader;


/**
 * Http reader test class
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpReaderTest {

	@Test
	public void readLineTest() {

		String basicHttpHeader = "HTTP/1.1 200 OK";

		InputStream inputstream = new ByteArrayInputStream(
				basicHttpHeader.getBytes());

		HttpReader reader = new HttpReader();

		try {
			assertTrue("http reader basic header",
					reader.readLine(inputstream).equals(basicHttpHeader));
		} catch (IOException e) {
			fail("IOException");
		}

		try {
			inputstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		basicHttpHeader = "HTTP/1.1 404 NOT FOUND";
		inputstream = new ByteArrayInputStream(basicHttpHeader.getBytes());

		reader = new HttpReader();

		try {
			assertTrue("http reader basic header",
					reader.readLine(inputstream).equals(basicHttpHeader));
		} catch (IOException e) {
			fail("IOException");
		}

		try {
			inputstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
