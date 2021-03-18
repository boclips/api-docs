package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.BearerTokenDocumentationPolicy
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyParameters
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes
import java.util.regex.Pattern

class LoginDocTests : AbstractDocTests() {
    @Test
    fun `refresh token flow`() {
        given(loginDocumentationSpec).urlEncodingEnabled(true)
            .param("grant_type", "refresh_token")
            .param("client_id", "hq")
            .param("refresh_token", publicClientRefreshToken)
            .filter(
                document(
                    "refresh-token-example",
                    preprocessRequest(
                        modifyParameters().set("client_id", "***").set("refresh_token", "***")
                    ),
                    preprocessResponse(
                        replacePattern(
                            Pattern.compile("\"access_token\"\\s*:\\s*\"[^\"]+\""),
                            "\"access_token\" : \"***\""
                        ),
                        replacePattern(
                            Pattern.compile("\"refresh_token\"\\s*:\\s*\"[^\"]+\""),
                            "\"refresh_token\" : \"***\""
                        )
                    ),
                    tokenResponseFields,
                    requestParameters(
                        parameterWithName("grant_type").description("The grant type for this flow must always be `refresh_token`").attributes(
                            Attributes.key("type").value("String - Constant")
                        ),
                        parameterWithName("client_id").description("The client ID that you've been issued with").attributes(
                            Attributes.key("type").value("String")
                        ),
                        parameterWithName("refresh_token").description("The `refresh_token` that was obtained before").attributes(
                            Attributes.key("type").value("String")
                        )
                    )
                )
            )
            .`when`().post("/token").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }

    @Test
    fun `authorization code flow - requesting code`() {
        given(loginDocumentationSpec).urlEncodingEnabled(true)
            .param("response_type", "code")
            .param("client_id", "teachers")
            .param("redirect_uri", "https://teachers.staging-boclips.com")
            .filter(
                document(
                    "authorization-code-example",
                    preprocessRequest(
                        modifyParameters().set("client_id", "***").set("redirect_uri", "***")
                    ),
                    requestParameters(
                        parameterWithName("response_type").description("The response type for this flow must always be `code`").attributes(
                            Attributes.key("type").value("String - Constant")
                        ),
                        parameterWithName("client_id").description("The client ID that you've been issued with").attributes(
                            Attributes.key("type").value("String")
                        ),
                        parameterWithName("redirect_uri").description("The URL your user should be redirected to once we managed to authorize her. Typically the root of your webapp. We need to whitelist valid redirect URLs on our end, please let us know where your app will be hosted.").attributes(
                            Attributes.key("type").value("URL")
                        )
                    )
                )
            )
            .`when`().get("/authorize").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }

    @Test
    fun `authorization code flow - token request`() {
        given(loginDocumentationSpec).urlEncodingEnabled(true)
            .param("grant_type", "authorization_code")
            .param("client_id", "viewsonic")
            .param("code", "***")
            .param("redirect_uri", "exact-same-url-used-to-request-code")
            .filter(
                document(
                    "authorization-code-token-example",
                    preprocessRequest(
                        modifyParameters().set("client_id", "***")
                    ),
                    requestParameters(
                        parameterWithName("grant_type").description("The grant type for this flow must always be `authorization_code`").attributes(
                            Attributes.key("type").value("String - Constant")
                        ),
                        parameterWithName("client_id").description("The client ID that you've been issued with").attributes(
                            Attributes.key("type").value("String")
                        ),
                        parameterWithName("code").description("The client secret that was Boclips issued to you").attributes(
                            Attributes.key("type").value("String")
                        ),
                        parameterWithName("redirect_uri").description("The exact same redirect_uri that was used when requesting the code. Including path and/or params if any.").attributes(
                            Attributes.key("type").value("String")
                        )
                    )
                )
            )
            .`when`().post("/token").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(400)).and()
    }

    @Test
    fun `client credentials flow`() {
        given(loginDocumentationSpec).urlEncodingEnabled(true)
            .param("grant_type", "client_credentials")
            .param("client_id", clientId)
            .param("client_secret", clientSecret)
            .filter(
                document(
                    "client-credentials-example",
                    preprocessRequest(
                        modifyParameters().set("client_id", "***").set("client_secret", "***")
                    ),
                    tokenResponseFields,
                    requestParameters(
                        parameterWithName("grant_type").description("The grant type for this flow must always be `client_credentials`").attributes(
                            Attributes.key("type").value("String - Constant")
                        ),
                        parameterWithName("client_id").description("The client ID that you've been issued with").attributes(
                            Attributes.key("type").value("String")
                        ),
                        parameterWithName("client_secret").description("The client secret that was Boclips issued to you").attributes(
                            Attributes.key("type").value("String")
                        )
                    )
                )
            )
            .`when`().post("/token").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }

    val tokenResponseFields: ResponseFieldsSnippet = responseFields(
        fieldWithPath("access_token").description("The OIDC `access_token` JWT-encoded that can be used to fetch Boclips resources"),
        fieldWithPath("refresh_token").description("The OIDC `refresh_token` that can be used to get a new `access_token` if the current expires"),
        fieldWithPath("expires_in").description("Expiration of the `access_token` in seconds"),
        fieldWithPath("refresh_expires_in").description("Expiration of the `refresh_token` in millis"),

        fieldWithPath("scope").description("https://auth0.com/docs/scopes/current/oidc-scopes[OIDC claims] allowed for this particular `access_token`"),
        fieldWithPath("session_state").optional().description("https://openid.net/specs/openid-connect-session-1_0.html#CreatingUpdatingSessions[OIDC Session State] - only present if the OP supports session management"),
        fieldWithPath("not-before-policy").optional().description("The instant after which the `access_token` will become valid as long as it hasn't expired"),
        fieldWithPath("token_type").description("https://openid.net/specs/openid-connect-core-1_0.html[OIDC token type] must be `bearer`")
    )

    private lateinit var loginDocumentationSpec: RequestSpecification

    @BeforeEach
    fun setupDocumentationSpec(restDocumentation: RestDocumentationContextProvider) {
        loginDocumentationSpec = RequestSpecificationFactory.createFor(
            accessToken = publicClientAccessToken,
            restDocumentation = restDocumentation,
            bearerTokenDocumentationPolicy = BearerTokenDocumentationPolicy.HIDE
        )
    }
}

