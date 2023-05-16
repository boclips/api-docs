Parameter | Description
--------- | -----------
`response_type` | The response type for this flow must always be `code`
`client_id` | The client ID that you've been issued with
`redirect_uri` | The URL your user should be redirected to once we managed to authorize her. Typically the root of your webapp. We need to whitelist valid redirect URLs on our end, please let us know where your app will be hosted.
