Path | Type | Description
---- | ---- | -----------
`title` | `String` | Collection's title
`description` | `String` | Collection's description
`videos` | `Array` | A list of IDs of videos that should belong to this collection. Will replace existing videos
`subjects` | `Array` | A list of IDs of subjects that should belong to this collection. Will replace existing subjects
`discoverable` | `Boolean` | Whether the new collection is visible in search and has been vetted by Boclips
`ageRange.min` | `Number` | The lower bound of age range this collection of videos is suitable for
`ageRange.max` | `Number` | The upper bound of age range this collection of videos is suitable for
`attachment` | `Object` | An optional <<resources-collections-attachments,attachment>> that can be added to this collection
