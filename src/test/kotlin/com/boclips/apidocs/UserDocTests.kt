package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import java.util.regex.Matcher
import java.util.regex.Pattern

object Descriptions {
    const val FIRSTNAME = "The first name of the user"
    const val LASTNAME = "The user's last name"
}

class UserDocTests : AbstractDocTests() {

    @Test
    fun `getting user profile`() {
        val responseFields = responseFields(
            fieldWithPath("id").description("The ID of the user"),
            fieldWithPath("firstName").description(Descriptions.FIRSTNAME),
            fieldWithPath("lastName").description(Descriptions.LASTNAME),
            fieldWithPath("email").description("The email of the user"),
            subsectionWithPath("_links").description("HAL links related to this collection")
        )
        val responseLinks = links(
            linkWithRel("profile").ignored(),
            linkWithRel("self").description("Points to this user profile")
        )

        val userId = extractUserId(links["profile"]!!)
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-user-get-user-profile",
                    responseFields,
                    responseLinks
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get the user's profile",
                    false,
                    pathParameters(
                        parameterWithName("id").description("The ID of the user")
                    ),
                    responseFields,
                    responseLinks
                )
            )
            .`when`()
            .get("/v1/users/{id}", userId)
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `updating user profile`(restDocumentation: RestDocumentationContextProvider) {
        val myLinks = getLinksFor(userAccessToken)

        val requestFields = requestFields(
            fieldWithPath("firstName").optional().description(Descriptions.FIRSTNAME),
            fieldWithPath("lastName").optional().description(Descriptions.LASTNAME),
        )
        val userId = extractUserId(myLinks["profile"]!!)

        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-user-put-user-profile",
                    requestFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Update the user's profile",
                    false,
                    requestFields
                )
            )
            .`when`()
            .contentType(ContentType.JSON)
            .body(
                """
                {
                    "firstName": "John",
                    "lastName": "Smith",
                }
                """.trimIndent()
            )
            .put("/v1/users/{id}", userId)
            .then()
            .assertThat().statusCode(`is`(200))
    }

    private fun extractUserId(url: String): String {
        val pattern: Pattern =
            Pattern.compile("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}")
        val matcher: Matcher = pattern.matcher(url)
        return if (matcher.find()) {
            matcher.group(0)
        } else {
            throw Exception("Could not find user ID in self link")
        }
    }

    @BeforeEach
    override fun setUp(restDocumentation: RestDocumentationContextProvider) {
        super.setUp(restDocumentation)
        stubOwnerSpec = RequestSpecificationFactory.createFor(userAccessToken, restDocumentation)
        links = getLinksFor(userAccessToken)
    }
}
