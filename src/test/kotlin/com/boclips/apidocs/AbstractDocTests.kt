package com.boclips.apidocs

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.regex.Pattern

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@ActiveProfiles("test")
abstract class AbstractDocTests {

    protected var documentationSpec: RequestSpecification? = null

    @Value("\${api.username}")
    lateinit var username: String

    @Value("\${api.password}")
    lateinit var password: String

    @Value("\${api.clientid}")
    lateinit var clientId: String

    @Value("\${api.clientsecret}")
    lateinit var clientSecret: String

    protected lateinit var accessToken: String
    protected lateinit var refreshToken: String

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val payload = Fuel.post(
                "https://api.staging-boclips.com/v1/token", listOf(
                "grant_type" to "password",
                "client_id" to "teachers",
                "username" to username,
                "password" to password
        )
        ).responseObject<Map<String, Any>>().third.component1()
        accessToken = (payload?.get("access_token") as String?) ?: ""
        refreshToken = (payload?.get("refresh_token") as String?) ?: ""

        this.documentationSpec = RequestSpecBuilder()
            .setBaseUri("https://api.staging-boclips.com/v1")
            .addHeader("Authorization", "Bearer $accessToken")
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        modifyUris()
                            .scheme("https")
                            .host("api.boclips.com"),
                        removeHeaders("Authorization"),
                        replacePattern(Pattern.compile("staging-boclips"), "boclips"),
                        prettyPrint()
                    )
                    .withResponseDefaults(modifyUris()
                        .scheme("https")
                        .host("api.boclips.com"),
                        removeHeaders("Authorization"),
                        replacePattern(Pattern.compile("staging-boclips"), "boclips"),
                        prettyPrint()
                    )
            ).build()
    }

    val linksFieldDescriptor = PayloadDocumentation.subsectionWithPath("_links").description("HAL links for this resource")

}
