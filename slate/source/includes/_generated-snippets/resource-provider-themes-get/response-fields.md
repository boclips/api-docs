Path | Type | Description
---- | ---- | -----------
`_embedded.themes[].id` | `String` | ID of the theme
`_embedded.themes[].provider` | `String` | Name of the curriculum or publisher
`_embedded.themes[]._links` | `Object` | HAL links for the resource
`_embedded.themes[].type` | `String` | The discipline or school level
`_embedded.themes[].title` | `String` | The book or specific grade level
`_embedded.themes[].topics[].index` | `Number` | Recommended order of the topic
`_embedded.themes[].topics[].title` | `String` | The chapter or cluster name
`_embedded.themes[].topics[].targets[].index` | `Number` | Recommended order of the target
`_embedded.themes[].topics[].targets[].title` | `String` | The section or standard name
`_embedded.themes[].topics[].targets[].videoIds` | `Array` | Videos aligned to the target
