```http
HTTP/1.1 200 OK
X-Boclips-Trace-ID: ba15c89b0a263e03
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Vary: accept-encoding
Access-Control-Expose-Headers: *
Set-Cookie: JSESSIONID=B467D7E53A2BBD7D47BB534181953D16; Path=/; Secure; HttpOnly
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
Content-Encoding: gzip
Content-Type: application/hal+json;charset=UTF-8
Date: Tue, 16 May 2023 13:59:37 GMT
Via: 1.1 google
Alt-Svc: h3=":443"; ma=2592000,h3-29=":443"; ma=2592000
Transfer-Encoding: chunked
Content-Length: 380

{"id":"0690f7ee-d38f-48c5-863c-ea39bfdbc5d3","firstName":"John","lastName":"Smith","ages":[7,8,9],"subjects":[{"id":"5cb499c9fd5beb428189454b"}],"email":"updatable@user.com","_links":{"self":{"href":"https://api.staging-boclips.com/v1/users/0690f7ee-d38f-48c5-863c-ea39bfdbc5d3"},"profile":{"href":"https://api.staging-boclips.com/v1/users/0690f7ee-d38f-48c5-863c-ea39bfdbc5d3"}}}
```