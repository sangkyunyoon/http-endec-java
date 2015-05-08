package fr.bmartel.protocol.http.inter;

import java.util.HashMap;

import fr.bmartel.protocol.http.HttpReader;
import fr.bmartel.protocol.http.HttpVersion;
import fr.bmartel.protocol.http.utils.IByteList;

/**
 * Interface for http request frame template
 * 
 * @author Bertrand Martel
 * 
 */
public interface IHttpRequestFrame {

	/** http request version */
	public HttpVersion getHttpVersion();

	/**
	 * http reader permitting to read and parse http frame on inputstream
	 * channel
	 */
	public HttpReader getReader();

	/** http Host name */
	public String getHost();

	/** list of http headers */
	public HashMap<String, String> getHeaders();

	/** request method */
	public String getMethod();

	/** request uri */
	public String getUri();

	/** queryString for http request */
	public String getQueryString();

	/** request body content */
	public IByteList getBody();
}
