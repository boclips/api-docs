Relation | Description
-------- | -----------
`self` | Points to this collection
`edit` | `PATCH` requests can be sent to this URL to update the collection
`remove` | `DELETE` request can be sent to this URL to remove the collection (videos remain in the system)
`addVideo` | `PUT` requests to this URL allow to add more videos to this collection
`removeVideo` | `DELETE` requests to this URL allow to remove videos from this collection
`interactedWith` | `POST` requests to this URL to track collection interaction events
`updatePermissions` | `PATCH` requests to this URL allow to update collection permissions
