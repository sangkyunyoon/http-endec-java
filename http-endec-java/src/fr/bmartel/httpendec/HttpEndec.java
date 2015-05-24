/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Bertrand Martel
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.httpendec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import fr.bmartel.protocol.http.HttpFrame;
import fr.bmartel.protocol.http.HttpResponseFrame;
import fr.bmartel.protocol.http.HttpVersion;
import fr.bmartel.protocol.http.StatusCodeObject;
import fr.bmartel.protocol.http.constants.HttpMethod;
import fr.bmartel.protocol.http.inter.IHttpFrame;
import fr.bmartel.protocol.http.states.HttpStates;
import fr.bmartel.protocol.http.utils.ListOfBytes;

/**
 * @mainpage  HTTP Java encoder / decoder
 * 
 * 
 HTTP encoder/decoder in JAVA.

 * parse all your HTTP streaming

 * chunk your bufferized data into outputstream according to value `fr.bmartel.protocol.socket.DataBufferConst.DATA_BLOCK_SIZE_LIMIT` (you can change this value if you use a JVM which does not support this one)
 * All HTTP stream is encoded in UTF-8

 * You can build HTTP request/response independently from Http parser

 `http-endec-java`      : library source<br/>
 `http-endec-java-test` : test unit for library source


 */
/**
 * 
 * Test for http-endec-java project
 * 
 * @author Bertrand Martel
 *
 */
public class HttpEndec {

	/**
	 * Test cases for http encoder/decoder
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		HttpStates status = HttpStates.HTTP_STATE_NONE;

		System.out.println("HTTP REQUEST TEST ");
		/* Build custom HTTP Post request with specified headers */

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		HttpVersion version = new HttpVersion(1, 1);

		HttpFrame frameRequest = new HttpFrame(HttpMethod.POST_REQUEST,
				version, headers, "/rest/help/todo", new ListOfBytes(
						"kind of body"));

		System.out.println(frameRequest.toString());

		System.out.println("##########################################");
		System.out.println("HTTP RESPONSE TEST ");
		/* Build custom HTTP response with specified headers */

		headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		StatusCodeObject object = new StatusCodeObject(200, "OK");
		version = new HttpVersion(1, 1);

		HttpResponseFrame frameResponse = new HttpResponseFrame(object,
				version, headers, new byte[] {});

		System.out.println(frameResponse.toString());

		System.out.println("##########################################");

		String multipleFrame1 = "HTTP/1.1 200 OK\r\nheaders5:  value5\r\nheaders6:  value6\r\n\r\nHTTP/1.1 200 OK\r\n\r\n";
		String multipleFrame2 = "POST /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\nHTTP/1.1 200 OK\r\n\r\n";
		InputStream inputstream1 = new ByteArrayInputStream(
				multipleFrame1.getBytes());
		InputStream inputstream2 = new ByteArrayInputStream(
				multipleFrame2.getBytes());
		HttpFrame httpFrame = new HttpFrame();

		try {
			System.out.println("_____Multiple frame input 1____");
			status = httpFrame.parseHttp(inputstream1);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream1);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			System.out.println("_____Multiple frame input 2____");
			status = httpFrame.parseHttp(inputstream2);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream2);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		String multipleFrameWithError1 = "bodyTobeWritten\r\nHTTP/1.1 200 OK\r\n\r\nPOST /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\n";
		String multipleFrameWithError2 = "HTTP/1.1 200 OK\r\n\r\nqsdqsfefneifniznginzieg+1+2+121422°KEF0KEffpskdpfkHr\nPOST /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\n";
		String multipleFrameWithError3 = "HTTP/1.1 200 OK\r\n\r\nqsdqsfefneifniznginzieg+1+2+1214\r\n\r\n22°KEF0KEffpskdpfkHr\r\n\r\n\r\n\r\nPOST /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\n";
		String multipleFrameWithError4 = "HTTP/1.1 200 OK\r\n\r\nqsdqsfefneifniznginPOST /rest/help/todo HTTP/1.1\r\nheaders1:  \r\nzrezrzerzerzervalue1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\nHTTP/1.1 200 OK\r\n\r\n";

		InputStream inputstream3 = new ByteArrayInputStream(
				multipleFrameWithError1.getBytes());
		InputStream inputstream4 = new ByteArrayInputStream(
				multipleFrameWithError2.getBytes());
		InputStream inputstream5 = new ByteArrayInputStream(
				multipleFrameWithError3.getBytes());
		InputStream inputstream6 = new ByteArrayInputStream(
				multipleFrameWithError4.getBytes());

		try {
			System.out.println("_____Multiple frame with error input 1____");
			status = httpFrame.parseHttp(inputstream3);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream3);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream3);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			System.out.println("_____Multiple frame with error input 2____");
			status = httpFrame.parseHttp(inputstream4);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream4);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream4);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			System.out.println("_____Multiple frame with error input 3____");
			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream5);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			System.out.println("_____Multiple frame with error input 4____");
			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);

			status = httpFrame.parseHttp(inputstream6);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Test with chunked data");
		System.out.println("##########################################");

		String chunkedData = "HTTP/1.1 200 OK\r\nTransfer-Encoding:  chunked\r\n\r\n9\r\nabcdefghi\r\n1\r\nj\r\n";

		InputStream chunkIn = new ByteArrayInputStream(chunkedData.getBytes());

		try {
			status = httpFrame.parseHttp(chunkIn);
			HttpEndec.printHttpFrameDecodedResult(httpFrame, status);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print result of http decoding
	 * 
	 * @param frame
	 *            http frame object
	 * @param decodingStates
	 *            final states of http decoding (to catch http decoding error)
	 */
	public static void printHttpFrameDecodedResult(IHttpFrame frame,
			HttpStates decodingStates) {
		if (frame.isHttpRequestFrame()) {
			System.out.println("uri         : " + frame.getUri());
			System.out.println("version     : " + frame.getHttpVersion());
			System.out.println("method      : " + frame.getMethod());
			System.out.println("querystring : " + frame.getQueryString());
			System.out.println("host        : " + frame.getHost());
			System.out.println("chunked     : " + frame.isChunked());
			System.out.println("body        : "
					+ new String(frame.getBody().getBytes()));

			Set<String> keys = frame.getHeaders().keySet();
			Iterator<String> it = keys.iterator();
			int count = 0;
			while (it.hasNext()) {
				Object key = it.next();
				Object value = frame.getHeaders().get(key);
				System.out.println("headers n ° " + count + " : "
						+ key.toString() + " => " + value.toString());
			}
		} else if (frame.isHttpResponseFrame()) {
			System.out
					.println("status code         : " + frame.getStatusCode());
			System.out.println("reason phrase       : "
					+ frame.getReasonPhrase());
			System.out.println("chunked             : " + frame.isChunked());
			System.out.println("body                : "
					+ new String(frame.getBody().getBytes()));

			Set<String> keys = frame.getHeaders().keySet();
			Iterator<String> it = keys.iterator();
			int count = 0;
			while (it.hasNext()) {
				Object key = it.next();
				Object value = frame.getHeaders().get(key);
				System.out.println("headers n ° " + count + " : "
						+ key.toString() + " => " + value.toString());
			}
		} else {
			System.out
					.println("Error, this http frame has not beed decoded correctly. Error code : "
							+ decodingStates.toString());
		}
		System.out.println("##########################################");
	}
}
