```http
HTTP/1.1 400 Bad Request
X-Boclips-Trace-ID: 8a30e111f9ea97f9
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Access-Control-Expose-Headers: *
Set-Cookie: JSESSIONID=B89185FCDE236D945851C903A6898F27; Path=/; Secure; HttpOnly
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
Content-Type: application/json
Date: Tue, 16 May 2023 14:02:18 GMT
content-encoding: gzip
Via: 1.1 google
Alt-Svc: h3=":443"; ma=2592000,h3-29=":443"; ma=2592000
Transfer-Encoding: chunked
Content-Length: 202

{"path":"/v1/videos","status":400,"timestamp":"2023-05-16T14:02:19.920151929Z","error":"Invalid request","message":"not-quite-a-number is not a valid ISO 8601 duration. Example is PT5S.","reasons":null}
```