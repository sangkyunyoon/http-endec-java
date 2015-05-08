package fr.bmartel.protocol.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import fr.bmartel.protocol.http.constants.HttpConstants;
import fr.bmartel.protocol.http.constants.HttpHeader;
import fr.bmartel.protocol.http.constants.HttpMethod;
import fr.bmartel.protocol.http.inter.IHttpRequestFrame;
import fr.bmartel.protocol.http.utils.HttpErrorCode;
import fr.bmartel.protocol.http.utils.IByteList;
import fr.bmartel.protocol.http.utils.ListOfBytes;
import fr.bmartel.protocol.socket.DataBufferConst;

/**
 * Http request frame builder and parser
 * 
 * @author Bertrand Martel
 * 
 */
public class HttpFrame implements IHttpRequestFrame {

	/** http request version */
	private HttpVersion httpVersion = new HttpVersion(1, 1);

	/**
	 * http reader object used to extract http request/response frame from
	 * inputstream
	 */
	private HttpReader reader = new HttpReader();

	/** http Host name */
	private String host = "";

	/** list of http headers */
	private HashMap<String, String> headers = new HashMap<String, String>();

	/** request method */
	private String method = "";

	/** request uri */
	private String uri = "";

	/** queryString for http request */
	private String queryString = "";

	/** request body content */
	private IByteList body = new ListOfBytes();

	/** Default constructor used in parse */
	public HttpFrame() {
		this.reader = new HttpReader();
	}

	/**
	 * Http builder with all parameters (RFC)
	 * 
	 * @param method
	 *            request method
	 * @param httpVersion
	 *            http version
	 * @param headers
	 *            request http headers
	 * @param uri
	 *            request uri
	 * @param body
	 *            request body content
	 */
	public HttpFrame(String method, HttpVersion httpVersion,
			HashMap<String, String> headers, String uri, IByteList body) {
		this.httpVersion = httpVersion;
		this.headers = headers;
		this.method = method;
		this.uri = uri;
		this.body = body;
	}

	/**
	 * Format request to string to be sent to outputStream (be careful NOT to
	 * insert space between header name and ":")
	 */
	public String toString() {
		String ret = "";
		if (!this.method.equals("")) {
			ret = this.method + " " + this.uri + " "
					+ this.httpVersion.toString()
					+ HttpConstants.HEADER_DELEMITER;
		} else {
			ret = this.uri + " " + this.httpVersion.toString()
					+ HttpConstants.HEADER_DELEMITER;
		}
		Set<String> cles = this.headers.keySet();
		Iterator<String> it = cles.iterator();
		while (it.hasNext()) {
			Object cle = it.next();
			Object valeur = this.headers.get(cle);
			ret += cle.toString() + HttpConstants.HEADER_VALUE_DELIMITER + " "
					+ valeur.toString() + HttpConstants.HEADER_DELEMITER;
		}
		ret += HttpConstants.HEADER_DELEMITER;
		try {
			ret += new String(body.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ret += HttpConstants.HEADER_DELEMITER;
		return ret;
	}

	public int parseHttp(InputStream in) throws IOException,
			InterruptedException {
		try {
			int code = -1;
			synchronized (in) {
				code = parseRequestLine(in);
				/* parse header */
				parseHeader(in);
				/* parse body request */
				parseBody(in);
			}
			// displayInformation();

			return code;
		} catch (SocketTimeoutException e) {
			return -1;
		}
	}

	/**
	 * Method used to parse inputstream data extracted from socket in order to
	 * retrieve method / uri / host and request body
	 * 
	 * @param inputstream
	 *            socket inputStream
	 * @return null if socket connection failed or bad request frame and String
	 *         terminating by '\r\n' if successfully parsed
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int parseRequestLine(InputStream inputstream) throws IOException,
			InterruptedException {

		String requestLine = null;

		/* read data from inputstream (until no data left on inputstream) */
		/* if data is not present return exception EOF */
		requestLine = this.reader.readLine(inputstream);
		if (requestLine == null) {
			return HttpErrorCode.HTTP_READING_ERROR;
		}

		if (!requestLine.contains("HTTP")) {
			return HttpErrorCode.HTTP_MALFORMED_FRAME;
		}

		/* identify key with delimeter space " " */
		StringTokenizer st = new StringTokenizer(requestLine, " ");

		String firstToken = st.nextToken();
		if (firstToken.equals(HttpMethod.POST_REQUEST)
				|| firstToken.equals(HttpMethod.GET_REQUEST)
				|| firstToken.equals(HttpMethod.OPTIONS_REQUEST)
				|| firstToken.equals(HttpMethod.DELETE_REQUEST)
				|| firstToken.equals(HttpMethod.PUT_REQUEST)) {
			this.method = firstToken;
			this.uri = st.nextToken();
		} else if (firstToken.startsWith("HTTP/")) {
			this.httpVersion = new HttpVersion(firstToken);
			if (httpVersion.versionDigit1 == 0) {
				return 400;
			}
			this.uri = st.nextToken();
		} else {
			return 400;
		}
		return 200;
	}

	/**
	 * Parse header putting all headers keys and values found into a map object
	 * 
	 * @param inputstream
	 *            socket inputstream
	 * @throws IOException
	 */
	private HashMap<String, String> parseHeader(InputStream inputstream)
			throws IOException {

		for (String s = this.reader.readLine(inputstream); (s != null)
				&& (s.length() != 0); s = this.reader.readLine(inputstream)) {
			/* identify separator ":" of fields */
			int idx = s.indexOf(":");

			if (idx > 0) {

				/* extract the key (to the left of ":" separator) */
				String header = s.substring(0, idx).trim();
				/* extract the value (to the right of ":" separator) */
				String value = s.substring(idx + 1).trim();
				/* key must be set in lower case */
				String key = header.toLowerCase();
				this.headers.put(key, value);
			}
		}

		/* get host headers and set object value */
		if (this.headers.containsKey(HttpHeader.HOST.toLowerCase())) {
			this.host = this.headers.get(HttpHeader.HOST.toLowerCase())
					.toString();
		}
		return this.headers;
	}

	/**
	 * parse request body only in order to set value of requestBody String
	 * 
	 * @param inputstream
	 *            inputstream used to read data from socket object
	 * @throws IOException
	 */
	public IByteList parseBody(InputStream inputstream) throws IOException {

		/* identify content length */
		int length = getContentLength();
		if (length > 0) {
			int numberOfBlockToWrite = length
					% DataBufferConst.DATA_BLOCK_SIZE_LIMIT;
			/* define number of block to write */
			int numberOfBlock = 0;
			if (numberOfBlockToWrite == 0) {
				numberOfBlock = length / DataBufferConst.DATA_BLOCK_SIZE_LIMIT;
			} else {
				numberOfBlock = (length / DataBufferConst.DATA_BLOCK_SIZE_LIMIT) + 1;
			}
			ListOfBytes list = new ListOfBytes();
			for (int i = 0; i < numberOfBlock; i++) {
				if (numberOfBlockToWrite != 0 && i == (numberOfBlock - 1)) {
					/* this is the last block to write */
					int size = length - i
							* DataBufferConst.DATA_BLOCK_SIZE_LIMIT;
					byte[] data = new byte[size];
					for (int j = 0; j < data.length; j++) {
						byte byteToBeRead = (byte) inputstream.read();
						data[j] = byteToBeRead;
					}
					list.add(data);
				} else {
					/* this is not the last block to write */
					byte[] data = new byte[DataBufferConst.DATA_BLOCK_SIZE_LIMIT];
					for (int j = 0; j < data.length; j++) {
						byte byteToBeRead = (byte) inputstream.read();
						data[j] = byteToBeRead;
					}
					list.add(data);
				}
			}
			this.body = list;
		} else {
			this.body = new ListOfBytes();
		}
		return this.body;
	}

	/**
	 * get Content-Length value
	 * 
	 * @return http content length
	 */
	public int getContentLength() {
		if (this.headers.containsKey(HttpHeader.CONTENT_LENGTH.toLowerCase())) {
			try {
				int length = Integer.parseInt(this.headers
						.get(HttpHeader.CONTENT_LENGTH.toLowerCase()));
				return length;
			} catch (NumberFormatException e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	@Override
	public HttpVersion getHttpVersion() {
		return this.httpVersion;
	}

	@Override
	public HttpReader getReader() {
		return this.reader;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public HashMap<String, String> getHeaders() {
		return this.headers;
	}

	/**
	 * Setter for headers
	 */
	public void setHeaders(HashMap<String, String> newHeaders) {
		this.headers = newHeaders;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public String getUri() {
		return this.uri;
	}

	@Override
	public String getQueryString() {
		return this.queryString;
	}

	@Override
	public IByteList getBody() {
		return this.body;
	}
}
