package com.boclips.apidocs.testsupport

import com.boclips.alignment.api.httpclient.AlignmentClient
import com.boclips.videos.api.httpclient.ChannelsClient
import com.boclips.videos.api.httpclient.CollectionsClient
import com.boclips.videos.api.httpclient.SubjectsClient
import com.boclips.videos.api.httpclient.VideosClient
import com.boclips.videos.api.httpclient.helper.ServiceAccountCredentials
import com.boclips.videos.api.httpclient.helper.ServiceAccountTokenFactory
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject
import feign.okhttp.OkHttpClient
import feign.opentracing.TracingClient
import io.jaegertracing.internal.JaegerTracer
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
import com.boclips.alignment.api.httpclient.helper.ServiceAccountCredentials as AlignmentServiceCredentials
import com.boclips.alignment.api.httpclient.helper.ServiceAccountTokenFactory as AlignmentServiceTokenFactory

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@ActiveProfiles("test")
abstract class AbstractDocTests {

    protected var stubOwnerSpec: RequestSpecification? = null
    protected var apiUserSpec: RequestSpecification? = null

    @Value("\${api.username}")
    lateinit var username: String

    @Value("\${api.password}")
    lateinit var password: String

    @Value("\${api.freshuser.username}")
    lateinit var freshUserUsername: String

    @Value("\${api.freshuser.password}")
    lateinit var freshUserPassword: String

    @Value("\${api.updatableuser.username}")
    lateinit var updatableUserUsername: String

    @Value("\${api.updatableuser.password}")
    lateinit var updatableUserPassword: String

    @Value("\${api.clientid}")
    lateinit var clientId: String

    @Value("\${api.clientsecret}")
    lateinit var clientSecret: String

    // credentials used in login spec only
    protected lateinit var publicClientAccessToken: String
    protected lateinit var publicClientRefreshToken: String

    // credentials used for operations not modifying the user
    protected lateinit var freshClientAccessToken: String
    protected lateinit var freshClientRefreshToken: String

    // credentials used for operations modifying the user
    protected lateinit var teacherAccessToken: String
    protected lateinit var teacherRefreshToken: String

    // credentials used to set up the fixtures
    protected lateinit var privateClientAccessToken: String
    protected lateinit var privateClientRefreshToken: String

    protected lateinit var videosClient: VideosClient
    protected lateinit var subjectsClient: SubjectsClient
    protected lateinit var collectionsClient: CollectionsClient
    protected lateinit var channelsClient: ChannelsClient
    protected lateinit var alignmentClient: AlignmentClient

    protected lateinit var links: Map<String, String>

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        setupPublicClientTokens()
        setupFreshClientTokens()
        setupTeacherToken()
        setupPrivateClientTokens()

        setupClients()
        setupLinks()

        stubOwnerSpec = RequestSpecificationFactory.createFor(privateClientAccessToken, restDocumentation)
        apiUserSpec = RequestSpecificationFactory.createFor(freshClientAccessToken, restDocumentation)
    }

    fun setupLinks() {
        links = getLinksFor(privateClientAccessToken)
    }

    protected fun getLinksFor(userToken: String): Map<String, String> {
        val linksResource = Fuel.get("https://api.staging-boclips.com/v1")
            .header("Authorization", "Bearer $userToken")
            .responseObject<Map<String, Any>>().third.component1()!!

        @Suppress("UNCHECKED_CAST")
        return (linksResource["_links"] as Map<String, Map<String, String>>)
            .mapValues { (it.value)["href"] as String }
    }

    private fun setupPublicClientTokens() {
        val payload = Fuel.post(
            "https://api.staging-boclips.com/v1/token",
            listOf(
                "grant_type" to "password",
                "client_id" to "hq",
                "username" to username,
                "password" to password
            )
        ).responseObject<Map<String, Any>>().third.component1()
        publicClientAccessToken = (payload?.get("access_token") as String?) ?: ""
        publicClientRefreshToken = (payload?.get("refresh_token") as String?) ?: ""
    }

    private fun setupFreshClientTokens() {
        val payload = Fuel.post(
            "https://api.staging-boclips.com/v1/token",
            listOf(
                "grant_type" to "password",
                "client_id" to "hq",
                "username" to freshUserUsername,
                "password" to freshUserPassword
            )
        ).responseObject<Map<String, Any>>().third.component1()
        freshClientAccessToken = (payload?.get("access_token") as String?) ?: ""
        freshClientRefreshToken = (payload?.get("refresh_token") as String?) ?: ""
    }

    private fun setupTeacherToken() {
        val payload = Fuel.post(
            "https://api.staging-boclips.com/v1/token",
            listOf(
                "grant_type" to "password",
                "client_id" to "teachers",
                "username" to updatableUserUsername,
                "password" to updatableUserPassword
            )
        ).responseObject<Map<String, Any>>().third.component1()
        teacherAccessToken = (payload?.get("access_token") as String?) ?: ""
        teacherRefreshToken = (payload?.get("refresh_token") as String?) ?: ""
    }

    private fun setupPrivateClientTokens() {
        val payload = Fuel.post(
            "https://api.staging-boclips.com/v1/token",
            listOf(
                "grant_type" to "client_credentials",
                "client_id" to clientId,
                "client_secret" to clientSecret
            )
        ).responseObject<Map<String, Any>>().third.component1()
        privateClientAccessToken = (payload?.get("access_token") as String?) ?: ""
        privateClientRefreshToken = (payload?.get("refresh_token") as String?) ?: ""
    }

    private fun setupClients() {
        videosClient = VideosClient.create(
            apiUrl = "https://api.staging-boclips.com",
            tokenFactory = ServiceAccountTokenFactory(
                ServiceAccountCredentials(
                    authEndpoint = "https://api.staging-boclips.com",
                    clientId = clientId,
                    clientSecret = clientSecret
                )
            ),
            feignClient = TracingClient(OkHttpClient(), JaegerTracer.Builder("api-docs").build())
        )

        collectionsClient = CollectionsClient.create(
            apiUrl = "https://api.staging-boclips.com",
            tokenFactory = ServiceAccountTokenFactory(
                ServiceAccountCredentials(
                    authEndpoint = "https://api.staging-boclips.com",
                    clientId = clientId,
                    clientSecret = clientSecret
                )
            ),
            feignClient = TracingClient(OkHttpClient(), JaegerTracer.Builder("api-docs").build())
        )

        subjectsClient = SubjectsClient.create(
            apiUrl = "https://api.staging-boclips.com",
            tokenFactory = ServiceAccountTokenFactory(
                ServiceAccountCredentials(
                    authEndpoint = "https://api.staging-boclips.com",
                    clientId = clientId,
                    clientSecret = clientSecret
                )
            ),
            feignClient = TracingClient(OkHttpClient(), JaegerTracer.Builder("api-docs").build())
        )

        channelsClient = ChannelsClient.create(
            apiUrl = "https://api.staging-boclips.com",
            tokenFactory = ServiceAccountTokenFactory(
                ServiceAccountCredentials(
                    authEndpoint = "https://api.staging-boclips.com",
                    clientId = clientId,
                    clientSecret = clientSecret
                )
            ),
            feignClient = TracingClient(OkHttpClient(), JaegerTracer.Builder("api-docs").build())
        )

        alignmentClient = AlignmentClient.create(
            apiUrl = "https://api.staging-boclips.com",
            tokenFactory = AlignmentServiceTokenFactory(
                AlignmentServiceCredentials(
                    authEndpoint = "https://api.staging-boclips.com",
                    clientId = clientId,
                    clientSecret = clientSecret
                )
            ),
            feignClient = TracingClient(OkHttpClient(), JaegerTracer.Builder("api-docs").build())
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
