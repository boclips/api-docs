# API Docs

This is our living API documentation project. Docs are generated automatically via [REST Assured](http://rest-assured.io/) based test suites and [Asciidoctor](https://asciidoctor.org/). 

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

Alternatively, you can serve the docs in hot-reload mode:

```
$ ./develop
```

This will build the docs, expose them on your [local](http://localhost:8080) and watch for changes to any tests or `*.adoc` files. Changes will trigger an automatic rebuild. Note that you need [`parallel`](https://www.gnu.org/software/parallel/) available in your shell for the script to work.
