package fr.bmartel.protocol.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import fr.bmartel.protocol.http.constants.HttpConstants;
import fr.bmartel.protocol.http.inter.IHttpResponseFrame;

/**
 * HTTP response frame builder/parser
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpResponseFrame implements IHttpResponseFrame {

	/** return code for response frame */
	private StatusCodeObject returnCode;

	/** http version for response frame */
	private HttpVersion httpVersion;

	/** headers for response frame */
	private HashMap<String, String> headers = new HashMap<String, String>();

	/**
	 * fix method value for http response frame (only used in websocket here)
	 */
	private String methodVal = "";

	/**
	 * Builder for http response frame
	 * 
	 * @param returnCode
	 *            status return code
	 * @param httpVersion
	 *            http version
	 * @param headers
	 *            hashmap of all headers
	 */
	public HttpResponseFrame(StatusCodeObject returnCode,
			HttpVersion httpVersion, HashMap<String, String> headers,
			String methodVal) {
		this.returnCode = returnCode;
		this.httpVersion = httpVersion;
		this.headers = headers;
		this.methodVal = methodVal;
	}

	/**
	 * Format request to string to be sent to outputStream (be careful NOT to
	 * insert space between header name and ":")
	 */
	public String toString() {
		String ret = this.httpVersion.toString() + " "
				+ this.returnCode.toString() + HttpConstants.HEADER_DELEMITER;
		Set<String> cles = this.headers.keySet();
		Iterator<String> it = cles.iterator();
		while (it.hasNext()) {
			Object cle = it.next();
			Object valeur = this.headers.get(cle);
			ret += cle.toString() + HttpConstants.HEADER_VALUE_DELIMITER + " "
					+ valeur.toString() + HttpConstants.HEADER_DELEMITER;
		}
		ret += HttpConstants.HEADER_DELEMITER;
		return ret;
	}

	@Override
	public StatusCodeObject getReturnCode() {
		return this.returnCode;
	}

	@Override
	public HttpVersion getHttpVersion() {
		return this.httpVersion;
	}

	@Override
	public HashMap<String, String> getHeaders() {
		return this.headers;
	}

	public String getMethodVal() {
		return methodVal;
	}

	public void setMethodVal(String methodVal) {
		this.methodVal = methodVal;
	}
}
