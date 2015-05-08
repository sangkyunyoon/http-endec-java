# JAVA HTTP encoder / decoder  #

http://akinaru.github.io/http-endec-java/

<i>Last update 08/05/2015</i>

HTTP encoder/decoder in JAVA.

* parse all your HTTP streaming

* chunk your bufferized data into outputstream according to value `fr.bmartel.protocol.socket.DataBufferConst.DATA_BLOCK_SIZE_LIMIT` (you can change this value if you use a JVM which does not support this one)
* All HTTP stream is encoded in UTF-8

* You can build HTTP request/response independently from Http parser

`http-endec-java`      : library source<br/>
`http-endec-java-test` : test unit for library source

<hr/>

<b>Build HTTP request</b>

`HttpFrame frameRequest = new HttpFrame(`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`								HttpMethod.POST_REQUEST      ,`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`								new HttpVersion(1, 1)        ,`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`								new HashMap<String, String>(),`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`								"/rest/help/todo"            ,`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`								new ListOfBytes("kind of body")`<br/>
`							);`

`frameRequest.toString()` will print : 

`POST /rest/help/todo HTTP/1.1`<br/>
`` `` <br/>
``kind of body``<br/>
`` ``<br/>
`` ``<br/>

<hr/>

<b>Build HTTP response</b>

`HttpResponseFrame frameResponse = new HttpResponseFrame(`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`new StatusCodeObject(200, "OK"),`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`new HttpVersion(1, 1),`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`new HashMap<String, String>(),`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`""`<br/>
`);`

`frameResponse.toString()` will print : 

`HTTP/1.1 200 OK`<br/>
`` ``<br/>
`` ``<br/>

<hr/>

<b>Parse your HTTP inputstream</b>

`HttpFrame newFrame = new HttpFrame();`

`newFrame.parseHttp(inputstream);`

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
