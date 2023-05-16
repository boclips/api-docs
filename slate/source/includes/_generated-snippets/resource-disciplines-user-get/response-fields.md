Path | Type | Description
---- | ---- | -----------
`_embedded.disciplines[].id` | `String` | Id of the discipline
`_embedded.disciplines[].name` | `String` | Name of the discipline
`_embedded.disciplines[].code` | `String` | kebab-case version of the name
`_embedded.disciplines[]._links` | `Object` | HAL links for the individual disciplines
`_embedded.disciplines[].subjects` | `Array` | A list of <<resources-subjects, subjects>> associated to this discipline
`_links` | `Object` | HAL links for the discipline collection resource
