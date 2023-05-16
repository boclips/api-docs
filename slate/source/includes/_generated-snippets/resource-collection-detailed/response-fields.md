Path | Type | Description
---- | ---- | -----------
`id` | `String` | The ID of the collection
`owner` | `String` | The ID of the collection's owner
`ownerName` | `String` | The name of the collection's owner
`title` | `String` | Collection's title
`description` | `String` | Collection's description
`videos` | `Array` | A list of <<resources-videos,videos>> in the collection. Shallow video details are returned by default
`subjects` | `Array` | A list of subjects assigned to this collection. See <<resources-subjects_response_fields,subjects>> for payload details
`updatedAt` | `String` | A timestamp of collection's last update
`discoverable` | `Boolean` | Discoverable collections are discoverable through searching and browsing.
`promoted` | `Boolean` | Whether the collection is promoted
`mine` | `Boolean` | Whether the collection belongs to me
`createdBy` | `String` | Name of collection's creator
`subjects` | `Array` | A list of teaching subjects this collection relates to
`ageRange` | `Null` | Tells which ages videos in this collection are suitable for
`attachments` | `Array` | A list of <<resources-collections-attachments,attachments>> linked to this collection
`_links` | `Object` | HAL links related to this collection
