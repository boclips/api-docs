package com.boclips.apidocs.testsupport.preprocessors

import com.boclips.apidocs.testsupport.preprocessors.ParameterReplacingPreprocessor.Companion.replaceParameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationRequestFactory
import java.net.URI

class ParameterReplacingPreprocessorTest {
    private val requestFactory = OperationRequestFactory()

    @Test
    fun `masks parameters in a form body`() {
        val request =
            request(
                method = HttpMethod.POST,
                contentType = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=ISO-8859-1"),
                body = "grant_type=client_credentials&client_id=real-id&client_secret=real-secret",
            )

        val masked = replaceParameters("client_id" to "***", "client_secret" to "***").preprocess(request)

        assertThat(masked.contentAsString).isEqualTo("grant_type=client_credentials&client_id=***&client_secret=***")
        assertThat(masked.headers.contentLength).isEqualTo(masked.content.size.toLong())
    }

    @Test
    fun `masks parameters in a query string`() {
        val request =
            request(
                uri = "https://api.test/v1/authorize?response_type=code&client_id=real-id&redirect_uri=https%3A%2F%2Fapp.test",
            )

        val masked = replaceParameters("client_id" to "***", "redirect_uri" to "***").preprocess(request)

        assertThat(masked.uri.rawQuery).isEqualTo("response_type=code&client_id=***&redirect_uri=***")
    }

    @Test
    fun `leaves a body that is not a form unchanged`() {
        val request =
            request(
                uri = "https://api.staging-boclips.com/v1/token?client_id=real-id",
                method = HttpMethod.POST,
                contentType = MediaType.APPLICATION_JSON,
                body = """{"client_id":"real-id"}""",
            )

        val masked = replaceParameters("client_id" to "***").preprocess(request)

        assertThat(masked.contentAsString).isEqualTo("""{"client_id":"real-id"}""")
        assertThat(masked.uri.rawQuery).isEqualTo("client_id=***")
    }

    @Test
    fun `fails when a parameter to mask is not in the request`() {
        val request = request(uri = "https://api.staging-boclips.com/v1/authorize?client_id=real-id")

        assertThrows<IllegalStateException> {
            replaceParameters("client_secret" to "***").preprocess(request)
        }
    }

    private fun request(
        uri: String = "https://api.staging-boclips.com/v1/token",
        method: HttpMethod = HttpMethod.GET,
        contentType: MediaType? = null,
        body: String = "",
    ): OperationRequest =
        requestFactory.create(
            URI(uri),
            method,
            body.toByteArray(),
            HttpHeaders().apply { contentType?.let { setContentType(it) } },
            emptyList(),
        )
}
