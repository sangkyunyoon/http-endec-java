package fr.bytel.hope.protocol.http.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

/**
 * Socket inputstream generating a Socket exception deliberatly
 * 
 * @author Bertrand Martel
 * 
 */
public class SocketInputstreamExceptionMock {

	private InputStream inputstream = null;

	/**
	 * build a socket inputstream exception mock object
	 */
	public SocketInputstreamExceptionMock() {
		this.setInputstream(new InputStream() {
			
			@Override
			public int read() throws IOException {
				throw new SocketException();
			}
		});
	}

	public InputStream getInputstream() {
		return inputstream;
	}

	public void setInputstream(InputStream inputstream) {
		this.inputstream = inputstream;
	}
}
