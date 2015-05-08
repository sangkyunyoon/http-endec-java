/**
 * 
 */
package fr.bytel.hope.protocol.http.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import fr.bmartel.protocol.http.HttpReader;
import fr.bytel.hope.protocol.http.mock.SocketInputstreamExceptionMock;

/**
 * Generate test for socket exception inside http reader inputstream
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpReaderSocketExceptionTest {

	@Test
	public void readLineTest() {
		/* inputstream with socket exception when reading */
		SocketInputstreamExceptionMock socket = new SocketInputstreamExceptionMock();
		InputStream inputstream = socket.getInputstream();
		HttpReader reader = new HttpReader();
		try {
			assertTrue("http inputstream socket exception",
					reader.readLine(inputstream) == null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
