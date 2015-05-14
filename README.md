# JAVA HTTP encoder / decoder  #

http://akinaru.github.io/http-endec-java/

<i>Last update 08/05/2015</i>

HTTP encoder/decoder in JAVA.

This endec works with streaming data in a socket-blocking configuration. 
It will block until the whole current HTTP frame is totally parsed.

* parse all your HTTP streaming

* chunk your bufferized data into outputstream according to value `fr.bmartel.protocol.socket.DataBufferConst.DATA_BLOCK_SIZE_LIMIT` (you can change this value if you use a JVM which does not support this one)
* All HTTP stream is encoded in UTF-8

* You can build HTTP request/response independently from Http parser

`http-endec-java`      : library source<br/>
`http-endec-java-test` : test unit for library source

<hr/>

<b>Build HTTP request</b>

```
HttpFrame frameRequest = new HttpFrame(
								HttpMethod.POST_REQUEST      ,
								new HttpVersion(1, 1)        ,
								new HashMap<String, String>(),
								"/rest/help/todo"            ,
								new ListOfBytes("kind of body")
							);
```

frameRequest.toString()` will print : 

```
POST /rest/help/todo HTTP/1.1

kind of body

```
<hr/>

<b>Build HTTP response</b>

```
HttpResponseFrame frameResponse = new HttpResponseFrame(
new StatusCodeObject(200, "OK"),
new HttpVersion(1, 1),
new HashMap<String, String>(),
""
);
```

`frameResponse.toString()` will print : 

```
HTTP/1.1 200 OK


```

<hr/>

<b>Parse your HTTP inputstream</b>

```
HttpFrame newFrame = new HttpFrame();

newFrame.parseHttp(inputstream);
```

You will access your result in sub method :

`newFrame.getUri()`<br/>
`newFrame.getHttpVersion()`<br/>
`newFrame.getMethod()`<br/>
`newFrame.getQueryString()`<br/>
`newFrame.getHost()`<br/>
`newFrame.getBody().getBytes()`<br/>

<hr/>

<b>Change your buffer size</b>

In `fr.bmartel.protocol.socket.DataBufferConst`

Change the value : 

`public final static int DATA_BLOCK_SIZE_LIMIT = 4089;`

to your likings

<hr/>

<b>Quick test command line syntax</b> 

``java -jar http-endec-1.0.jar``

in folder ./http-endec-java/release

<hr/>

* Project is JRE 1.7 compliant
* You can build it with ant => build.xml
* Development on Eclipse 
* Specification from RFC specified in https://www.ietf.org/rfc/rfc2616.txt
