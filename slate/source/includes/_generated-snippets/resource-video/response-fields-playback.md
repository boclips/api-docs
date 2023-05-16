Path | Type | Description
---- | ---- | -----------
`type` | `String` | Playback type, i.e. STREAM or YOUTUBE
`id` | `String` | Id of this playback, useful for YOUTUBE type
`duration` | `String` | Duration of this particular video in ISO-8601
`_links.createPlaybackEvent.href` | `String` | POST endpoint for a createPlaybackEvent. See more on events <<resources-events,here>>
`_links.createPlayerInteractedWithEvent.href` | `String` | POST endpoint for a createPlayerInteractedWithEvent
`_links.thumbnail.href` | `String` | Thumbnail URL for the video. May be <<overview-interpolating-urls,templated>> with thumbnailWidth
`_links.thumbnail.templated` | `Boolean` | Tells whether the thumbnail link is <<overview-interpolating-urls,templated>>
`_links.videoPreview.href` | `String` | VideoPreview URL for the video. Templated with thumbnailWidth, and thumbnailCount
`_links.hlsStream.href` | `String` | URL for the Apple HLS stream
