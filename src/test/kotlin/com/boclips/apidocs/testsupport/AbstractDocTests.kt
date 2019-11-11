package com.boclips.apidocs.testsupport

import com.boclips.videos.service.client.ServiceCredentials
import com.boclips.videos.service.client.VideoServiceClient
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

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

    protected lateinit var publicClientAccessToken: String
    protected lateinit var publicClientRefreshToken: String

    protected lateinit var privateClientAccessToken: String
    protected lateinit var privateClientRefreshToken: String

    protected lateinit var videoServiceClient: VideoServiceClient

    protected lateinit var links: Map<String, String>

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        setupPublicClientTokens()
        setupPrivateClientTokens()

        setupVideoServiceClient()

        setupLinks()

        documentationSpec = RequestSpecificationFactory.createFor(privateClientAccessToken, restDocumentation)
    }

    private fun setupLinks() {
        val linksResource = Fuel.get("https://api.staging-boclips.com/v1")
            .header("Authorization", "Bearer $privateClientAccessToken")
            .responseObject<Map<String, Any>>().third.component1()!!

        @Suppress("UNCHECKED_CAST")
        links = (linksResource["_links"] as Map<String, Map<String, String>>).mapValues { (it.value)["href"] as String }
    }

    private fun setupPublicClientTokens() {
        val payload = Fuel.post(
            "https://api.staging-boclips.com/v1/token", listOf(
                "grant_type" to "password",
                "client_id" to "teachers",
                "username" to username,
                "password" to password
            )
        ).responseObject<Map<String, Any>>().third.component1()
        publicClientAccessToken = (payload?.get("access_token") as String?) ?: ""
        publicClientRefreshToken = (payload?.get("refresh_token") as String?) ?: ""
    }

    private fun setupPrivateClientTokens() {
        val payload = Fuel.post(
            "https://api.staging-boclips.com/v1/token", listOf(
                "grant_type" to "client_credentials",
                "client_id" to clientId,
                "client_secret" to clientSecret
            )
        ).responseObject<Map<String, Any>>().third.component1()
        privateClientAccessToken = (payload?.get("access_token") as String?) ?: ""
        privateClientRefreshToken = (payload?.get("refresh_token") as String?) ?: ""
    }

    private fun setupVideoServiceClient() {
        videoServiceClient = VideoServiceClient.getApiClient(
            "https://api.staging-boclips.com",
            ServiceCredentials.builder()
                .accessTokenUri("https://api.staging-boclips.com/v1/token")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()
        )
    }

    val linksFieldDescriptor: FieldDescriptor =
        PayloadDocumentation.subsectionWithPath("_links").description("HAL links for this resource")

    val pageSpecificationResponseFields = arrayOf(
        fieldWithPath("page.size").description("Amount of resources in the current page"),
        fieldWithPath("page.totalElements").description("Total amount of resources for this search query across pages"),
        fieldWithPath("page.totalPages").description("Total amount of pages for this search query"),
        fieldWithPath("page.number").description("Number of the current page. Zero-index based")
    )
}
