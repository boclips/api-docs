package com.boclips.apidocs.testsupport.preprocessors

import org.springframework.http.MediaType
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationRequestFactory
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

class ParameterReplacingPreprocessor(
    private val replacements: Map<String, String>,
) : OperationPreprocessor {
    companion object {
        fun replaceParameters(vararg replacements: Pair<String, String>) = ParameterReplacingPreprocessor(replacements.toMap())
    }

    private val requestFactory = OperationRequestFactory()

    override fun preprocess(request: OperationRequest): OperationRequest {
        val replaced = mutableSetOf<String>()
        val withQuery =
            requestFactory.create(
                replaceInQuery(request.uri, replaced),
                request.method,
                request.content,
                request.headers,
                request.parts,
                request.cookies,
            )
        val isForm = request.headers.contentType?.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED) == true
        val masked =
            if (isForm) requestFactory.createFrom(withQuery, replace(request.contentAsString, replaced).toByteArray()) else withQuery
        val missing = replacements.keys - replaced
        check(missing.isEmpty()) { "Parameters to mask are not in the request's query or form body: $missing" }
        return masked
    }

    override fun preprocess(response: OperationResponse): OperationResponse = response

    private fun replaceInQuery(
        uri: URI,
        replaced: MutableSet<String>,
    ): URI {
        val query = uri.rawQuery ?: return uri
        return UriComponentsBuilder
            .fromUri(uri)
            .replaceQuery(replace(query, replaced))
            .build(true)
            .toUri()
    }

    private fun replace(
        encodedParameters: String,
        replaced: MutableSet<String>,
    ): String =
        encodedParameters.split("&").joinToString("&") { pair ->
            val encodedName = pair.substringBefore("=")
            val name = URLDecoder.decode(encodedName, UTF_8)
            replacements[name]
                ?.let {
                    replaced += name
                    "$encodedName=${URLEncoder.encode(it, UTF_8)}"
                }
                ?: pair
        }
}
