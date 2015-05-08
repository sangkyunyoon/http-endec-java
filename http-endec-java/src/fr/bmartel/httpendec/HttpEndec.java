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
	public static void main(String[] args)
	{
		
		/* Build custom HTTP Post request with specified headers*/
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		HttpVersion version = new HttpVersion(1, 1);

		HttpFrame frameRequest = new HttpFrame(HttpMethod.POST_REQUEST, version, headers,
				"/rest/help/todo", new ListOfBytes("kind of body"));
		
		System.out.println(frameRequest.toString());
		
		System.out.println("##########################################");
		/*Build custom HTTP response with specified headers */
		
		headers = new HashMap<String, String>();
		headers.put("headers1", "value1");
		headers.put("headers2", "value2");

		StatusCodeObject object = new StatusCodeObject(200, "OK");
		version = new HttpVersion(1, 1);

		HttpResponseFrame frameResponse = new HttpResponseFrame(object, version,
				headers, "");
		
		System.out.println(frameResponse.toString());
		
		System.out.println("##########################################");
		/* Parse HTTP inputstream (Response test)*/
		String basicHttpHeader = "HTTP/1.1 200 OK";

		InputStream inputstream = new ByteArrayInputStream(
				basicHttpHeader.getBytes());

		HttpFrame newFrame = new HttpFrame();
		
		try {
			newFrame.parseHttp(inputstream);
			
			System.out.println("URI         : " + newFrame.getUri());
			System.out.println("VERSION     : " + newFrame.getHttpVersion());
			System.out.println("METHOD      : " + newFrame.getMethod());
			System.out.println("QUERYSTRING : " + newFrame.getQueryString());
			System.out.println("HOST        : " + newFrame.getHost());
			System.out.println("BODY        : " + new String(newFrame.getBody().getBytes()));

			Set<String> keys = newFrame.getHeaders().keySet();
			Iterator<String> it = keys.iterator();
			int count = 0;
			while (it.hasNext()) {
				Object key = it.next();
				Object value = newFrame.getHeaders().get(key);
				System.out.println("HEADERS n ° " + count + " : " + key.toString()
						+ " => " + value.toString());
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("##########################################");
		/* Parse HTTP inputstream (Request test)*/
		basicHttpHeader = "POST /rest/help/todo HTTP/1.1\r\nheaders1:  value1\r\nheaders2:  value2\r\nContent-Length:  15\r\n\r\nbodyTobeWritten\r\n";

		inputstream = new ByteArrayInputStream(
				basicHttpHeader.getBytes());

		newFrame = new HttpFrame();
		
		try {
			newFrame.parseHttp(inputstream);
			
			System.out.println("URI         : " + newFrame.getUri());
			System.out.println("VERSION     : " + newFrame.getHttpVersion());
			System.out.println("METHOD      : " + newFrame.getMethod());
			System.out.println("QUERYSTRING : " + newFrame.getQueryString());
			System.out.println("HOST        : " + newFrame.getHost());
			System.out.println("BODY        : " + new String(newFrame.getBody().getBytes()));

			Set<String> keys = newFrame.getHeaders().keySet();
			Iterator<String> it = keys.iterator();
			int count = 0;
			while (it.hasNext()) {
				Object key = it.next();
				Object value = newFrame.getHeaders().get(key);
				System.out.println("HEADERS n ° " + count + " : " + key.toString()
						+ " => " + value.toString());
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("##########################################");
	}
}
