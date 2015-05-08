/**
 * 
 */
package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import fr.bmartel.protocol.http.HttpReader;


/**
 * Bad inputstream test value
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpReaderTestBadInput {

	@Test
	public void readLineTest() {

		/* inputstream null */
		InputStream inputstream = null;

		HttpReader reader = new HttpReader();
		try {
			assertTrue("http inputstream null",
					reader.readLine(inputstream) == null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
