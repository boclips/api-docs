package com.boclips.apidocs

import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers
import org.imsglobal.lti.launch.LtiOauthSigner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyParameters
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.restdocs.snippet.Attributes.key
import java.util.regex.Pattern.compile

class LtiDocTests : AbstractDocTests() {
    val ltiOauthSigner = LtiOauthSigner()

    lateinit var ltiDocumentationSpec: RequestSpecification

    @Value("\${lti.v1p1.consumerkey}")
    lateinit var consumerKey: String

    @Value("\${lti.v1p1.consumersecret}")
    lateinit var consumerSecret: String

    @BeforeEach
    fun setupDocumentationSpec(restDocumentation: RestDocumentationContextProvider) {
        ltiDocumentationSpec = RequestSpecBuilder()
            .setBaseUri("https://lti.staging-boclips.com/v1p1")
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        modifyUris()
                            .scheme("https")
                            .host("lti.boclips.com"),
                        replacePattern(compile("staging-boclips"), "boclips"),
                        prettyPrint(),
                        modifyParameters().set("oauth_consumer_key", "your-consumer-key")
                    )
                    .withResponseDefaults(
                        modifyUris()
                            .scheme("https")
                            .host("lti.boclips.com"),
                        removeHeaders(
                            "Authorization",
                            "Pragma",
                            "Expires",
                            "X-Content-Type-Options",
                            "Date",
                            "X-Xss-Protection",
                            "Cache-Control"
                        ),
                        replacePattern(compile("staging-boclips"), "boclips"),
                        replacePattern(compile(consumerKey), "your-consumer-key"),
                        prettyPrint()
                    )
            )
            .build()
    }

    @Test
    fun `lti launch (based on videos endpoint)`() {
        val videoId = "5c542abf5438cdbcb56df0bf"

        val launchRequestParameters = mapOf(
            "lti_message_type" to "basic-lti-launch-request",
            "lti_version" to "LTI-1p0",
            "oauth_consumer_key" to consumerKey,
            "resource_link_id" to "41B464BA-F406-485C-ACDF-C1E5EB474156",
            "custom_logo" to "https://storage.googleapis.com/boclips-public-static-files/boclips/logo.png"
        )

        val signedLaunchRequestParameters = ltiOauthSigner.signParameters(
            launchRequestParameters,
            consumerKey,
            consumerSecret,
            "https://lti.staging-boclips.com/v1p1/videos/$videoId",
            "POST"
        )

        given(ltiDocumentationSpec)
            .redirects().follow(false)
            .also { spec ->
                signedLaunchRequestParameters.forEach { spec.param(it.key, it.value) }
            }
            .filter(
                document(
                    "lti-v1p1-full-launch-request",
                    requestParameters(
                        *this.ltiParameters,
                        *oauthParameters,
                        *boclipsParameters
                    )
                )
            )
            .filter(
                document(
                    "lti-v1p1-lti-parameters",
                    requestParameters(
                        *this.ltiParameters,
                        *oauthParametersIgnored,
                        *boclipsParametersIgnored
                    )
                )
            )
            .filter(
                document(
                    "lti-v1p1-oauth-parameters",
                    requestParameters(
                        *ltiParametersIgnored,
                        *oauthParameters,
                        *boclipsParametersIgnored
                    )
                )
            )
            .filter(
                document(
                    "lti-v1p1-boclips-parameters",
                    requestParameters(
                        *ltiParametersIgnored,
                        *oauthParametersIgnored,
                        *boclipsParameters
                    )
                )
            )
            .`when`()
            .post("/videos/{id}", videoId)
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(303))
    }

    val ltiParametersIgnored = arrayOf(
        parameterWithName("lti_message_type").ignored(),
        parameterWithName("lti_version").ignored(),
        parameterWithName("resource_link_id").ignored()
    )
    val ltiParameters = arrayOf(
        parameterWithName("lti_message_type")
            .description("The type of LTI message as defined in LTI spec. For our case it's a basic launch request and should be set to `basic-lti-launch-request`")
            .attributes(key("type").value("String")),
        parameterWithName("lti_version")
            .description("The LTI version. Per spec it should be set to `LTI-1p0` for LTI 1.1")
            .attributes(key("type").value("String")),
        parameterWithName("resource_link_id")
            .description("Generated by the consumer, uniquely identifies the launch link that initiated an LTI launch within the consumer application")
            .attributes(key("type").value("String"))
    )

    val oauthParametersIgnored = arrayOf(
        parameterWithName("oauth_nonce").ignored(),
        parameterWithName("oauth_signature").ignored(),
        parameterWithName("oauth_consumer_key").ignored(),
        parameterWithName("oauth_signature_method").ignored(),
        parameterWithName("oauth_timestamp").ignored(),
        parameterWithName("oauth_version").ignored()
    )
    val oauthParameters = arrayOf(
        parameterWithName("oauth_consumer_key")
            .description("The LTI consumer key. We create it and give it to you as a part of credentials exchange")
            .attributes(key("type").value("String")),
        parameterWithName("oauth_version")
            .description("OAuth version, should always be set to `1.0`")
            .attributes(key("type").value("String")),
        parameterWithName("oauth_signature_method")
            .description("Signature method, should be set to `HMAC-SHA1`")
            .attributes(key("type").value("String")),
        parameterWithName("oauth_signature")
            .description("The signature generated from request parameters and LTI consumer secret")
            .attributes(key("type").value("String")),
        parameterWithName("oauth_timestamp")
            .description("Request timestamp, expressed in the number of seconds since January 1, 1970 00:00:00 GMT. More details link:https://oauth.net/core/1.0/#nonce[here]")
            .attributes(key("type").value("Number")),
        parameterWithName("oauth_nonce")
            .description("A random value to help with uniquely identifying each request. More details link:https://oauth.net/core/1.0/#nonce[here]")
            .attributes(key("type").value("String"))
    )

    val boclipsParametersIgnored = arrayOf(
        parameterWithName("custom_logo").ignored()
    )
    val boclipsParameters = arrayOf(
        parameterWithName("custom_logo")
            .optional()
            .description("A URL to an image file that will be displayed at the top of LTI pages")
            .attributes(key("type").value("URL"))
    )
}