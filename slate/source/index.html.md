---
title: API Reference

language_tabs: # must be one of https://github.com/rouge-ruby/rouge/wiki/List-of-supported-languages-and-lexers
  - shell
  - ruby
  - python
  - javascript

toc_footers:
  - <a href='#'>Sign Up for a Developer Key</a>
  - <a href='https://github.com/slatedocs/slate'>Documentation Powered by Slate</a>

includes:
  - curl-request
  - errors
  - response-fields
  - alignments

search: true

code_clipboard: true

meta:
  - name: description
    content: Documentation for the Kittn API
---

# Introduction

Welcome to the Kittn API! You can use our API to access Kittn API endpoints, which can get information on various cats, kittens, and breeds in our database.

We have language bindings in Shell, Ruby, Python, and JavaScript! You can view code examples in the dark area to the right, and you can switch the programming language of the examples with the tabs in the top right.

This example API documentation page was created with [Slate](https://github.com/slatedocs/slate). Feel free to edit it and use it as a base for your own API's documentation.

# Authentication

> To authorize, use this code:
>
```bash
$ curl 'https://api.staging-boclips.com/v1/authorize?response_type=code&client_id=***&redirect_uri=***' -i -X GET \
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEVzg1cWVGSGp3dG9UN0Z2RkRwajJWelJyZTJRT3dsc2JPNHQ1blNWQXpFIn0.eyJleHAiOjE2ODQyNDU5MDAsImlhdCI6MTY4NDI0NTYwMCwianRpIjoiZDQzYmMzOWItOGRjYi00ZjkzLTlmYzAtY2E0Mzg3MzczNDk3IiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5zdGFnaW5nLWJvY2xpcHMuY29tL3JlYWxtcy9ib2NsaXBzIiwiYXVkIjpbInZpZGVvLXNlcnZpY2UiLCJhY2Nlc3Mtc2VydmljZSIsIm9yZGVyLXNlcnZpY2UiLCJ1c2VyLXNlcnZpY2UiLCJhbGlnbm1lbnQtc2VydmljZSIsImFjY291bnQiXSwic3ViIjoiMTY0MzQ0MmYtN2FkYy00MGY2LTk4NDUtN2QyN2Q3ZjRiZTE4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaHEiLCJzZXNzaW9uX3N0YXRlIjoiMjU3MzA1YzktZGU3OS00NDM3LTk0NTMtODU0ODhiMDBlY2Y1IiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbHRpLWRlbW8uc3RhZ2luZy1ib2NsaXBzLmNvbSIsImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImh0dHBzOi8vaHEuc3RhZ2luZy1ib2NsaXBzLmNvbSIsImh0dHBzOi8vYmFja29mZmljZS5zdGFnaW5nLWJvY2xpcHMuY29tIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJST0xFX0JPQ0xJUFNfV0VCX0FQUF9PUkRFUiIsIlJPTEVfQVBJIiwiUk9MRV9CT0NMSVBTX1dFQl9BUFBfQlJPV1NFIiwiUk9MRV9WSUVXU09OSUMiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ2aWRlby1zZXJ2aWNlIjp7InJvbGVzIjpbIlJPTEVfVklFV19WSURFT19SRUNPTU1FTkRBVElPTlMiLCJST0xFX0lOU0VSVF9DT0xMRUNUSU9OUyIsIlJPTEVfVklFV19DT0xMRUNUSU9OUyIsIlJPTEVfVklFV19WSURFT19UWVBFUyIsIlJPTEVfVEFHX1ZJREVPUyIsIlJPTEVfVklFV19ESVNDSVBMSU5FUyIsIlJPTEVfUkFURV9WSURFT1MiLCJST0xFX1ZJRVdfRURVQ0FUSU9OX0xFVkVMUyIsIlJPTEVfVklFV19DT05URU5UX1BBUlRORVJTIiwiUk9MRV9ET1dOTE9BRF9UUkFOU0NSSVBUIiwiUk9MRV9ERUxFVEVfQ09MTEVDVElPTlMiLCJST0xFX1VQREFURV9DT0xMRUNUSU9OUyIsIlJPTEVfSU5TRVJUX0VWRU5UUyIsIlJPTEVfVklFV19DSEFOTkVMUyIsIlJPTEVfVklFV19DT05URU5UX0NBVEVHT1JJRVMiLCJST0xFX1ZJRVdfVEFHUyIsIlJPTEVfVklFV19WSURFT1MiXX0sImFjY2Vzcy1zZXJ2aWNlIjp7InJvbGVzIjpbIlJPTEVfVklFV19BQ0NFU1NfUkVTT1VSQ0VTIl19LCJvcmRlci1zZXJ2aWNlIjp7InJvbGVzIjpbIlJPTEVfVklFV19BQ0NPVU5UX1BSSUNFUyIsIlJPTEVfVVBEQVRFX0NBUlRfSVRFTSIsIlJPTEVfQUREX0NBUlRfSVRFTVMiLCJST0xFX0RFTEVURV9DQVJUX0lURU1TIiwiUk9MRV9VUERBVEVfQ0FSVCIsIlJPTEVfVklFV19DQVJUIiwiUk9MRV9WSUVXX09XTl9PUkRFUlMiLCJST0xFX1BMQUNFX09SREVSIl19LCJ1c2VyLXNlcnZpY2UiOnsicm9sZXMiOlsiUk9MRV9TWU5DSFJPTklTRV9JTlRFR1JBVElPTl9VU0VSUyJdfSwiYWxpZ25tZW50LXNlcnZpY2UiOnsicm9sZXMiOlsiUk9MRV9WSUVXX0FMSUdOTUVOVFMiXX0sImFjY291bnQiOnsicm9sZXMiOlsidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiMjU3MzA1YzktZGU3OS00NDM3LTk0NTMtODU0ODhiMDBlY2Y1IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiYXBpIGFwaSIsInByZWZlcnJlZF91c2VybmFtZSI6ImFwaUBib2NsaXBzLmNvbSIsImdpdmVuX25hbWUiOiJhcGkiLCJmYW1pbHlfbmFtZSI6ImFwaSIsImVtYWlsIjoiYXBpQGJvY2xpcHMuY29tIn0.ci8vk90PyJ3iuYt7wK5OXmYtnXtaUFetTFtBiRgWGksOSMBy1SQXOk-cw7dPMWF0kzGzu6ZrcQOzevIC4a0C3kTbazGNpDFHyBcYR6TMN7izhSk61KjaLKeqK4X7Qb7ZcHBu2OxR0xIqbPoFu_TnCCUnD8IjfbsemQupio-aqMTl8EGTWtmPpI2rVoReOuB-t-t44bx1IV0xoXjpvT-Y6xauElJjRnQ2ump_RnQNVoZeUClN9tMlAcUm9UTh6hIyAALvdlAlumAKhmHSlF7Z7Eht-n-OPJ9r4Ec-jVV-FexS9pbqeCedY73R80XKjFC10HdSKOYAHcdR53VePYiuFg'
```
```ruby
require 'kittn'

api = Kittn::APIClient.authorize!('meowmeowmeow')
```

```python
import kittn

api = kittn.authorize('meowmeowmeow')
```

```shell
# With shell, you can just pass the correct header with each request
curl "api_endpoint_here" \
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
```

> Make sure to replace `meowmeowmeow` with your API key.

Kittn uses API keys to allow access to the API. You can register a new Kittn API key at our [developer portal](http://example.com/developers).

Kittn expects for the API key to be included in all API requests to the server in a header that looks like the following:

`Authorization: meowmeowmeow`

<aside class="notice">
You must replace <code>meowmeowmeow</code> with your personal API key.
</aside>

# Kittens

## Get All Kittens

```ruby
require 'kittn'

api = Kittn::APIClient.authorize!('meowmeowmeow')
api.kittens.get
```

```python
import kittn

api = kittn.authorize('meowmeowmeow')
api.kittens.get()
```

```shell
curl "http://example.com/api/kittens" \
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let kittens = api.kittens.get();
```

> The above command returns JSON structured like this:

```json
[
  {
    "id": 1,
    "name": "Fluffums",
    "breed": "calico",
    "fluffiness": 6,
    "cuteness": 7
  },
  {
    "id": 2,
    "name": "Max",
    "breed": "unknown",
    "fluffiness": 5,
    "cuteness": 10
  }
]
```

This endpoint retrieves all kittens.

### HTTP Request

`GET http://example.com/api/kittens`

### Query Parameters

Parameter | Default | Description
--------- | ------- | -----------
include_cats | false | If set to true, the result will also include cats.
available | true | If set to false, the result will include kittens that have already been adopted.

<aside class="success">
Remember — a happy kitten is an authenticated kitten!
</aside>

## Get a Specific Kitten

```ruby
require 'kittn'

api = Kittn::APIClient.authorize!('meowmeowmeow')
api.kittens.get(2)
```

```python
import kittn

api = kittn.authorize('meowmeowmeow')
api.kittens.get(2)
```

```shell
curl "http://example.com/api/kittens/2" \
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.get(2);
```

> The above command returns JSON structured like this:

```json
{
  "id": 2,
  "name": "Max",
  "breed": "unknown",
  "fluffiness": 5,
  "cuteness": 10
}
```

This endpoint retrieves a specific kitten.

<aside class="warning">Inside HTML code blocks like this one, you can't use Markdown, so use <code>&lt;code&gt;</code> blocks to denote code.</aside>

### HTTP Request

`GET http://example.com/kittens/<ID>`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the kitten to retrieve

## Delete a Specific Kitten

```ruby
require 'kittn'

api = Kittn::APIClient.authorize!('meowmeowmeow')
api.kittens.delete(2)
```

```python
import kittn

api = kittn.authorize('meowmeowmeow')
api.kittens.delete(2)
```

```shell
curl "http://example.com/api/kittens/2" \
  -X DELETE \
  -H "Authorization: meowmeowmeow"
```

```javascript
const kittn = require('kittn');

let api = kittn.authorize('meowmeowmeow');
let max = api.kittens.delete(2);
```

> The above command returns JSON structured like this:

```json
{
  "id": 2,
  "deleted" : ":("
}
```

This endpoint deletes a specific kitten.

### HTTP Request

`DELETE http://example.com/kittens/<ID>`

### URL Parameters

Parameter | Description
--------- | -----------
ID | The ID of the kitten to delete

