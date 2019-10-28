# API Docs

This is our living API documentation project. Docs are generated automatically via [REST-Assured](http://rest-assured.io/) based test suites and [Asciidoctor](https://asciidoctor.org/). 

## Development

Tests are executed against staging environment and the API client needs its credentials for that. You can set them up with:

```
$ ./setup
```

To generate the docs and expose them locally use this:

```
$ ./runWithDocs
```

This will serve the docs on your [local](http://localhost:8080).
