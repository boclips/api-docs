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
    const val firstName = "The first name of the user"
    const val lastName = "The user's last name"
    const val subjects =
        "Ids of teaching <<resources-subjects,subjects>> relevant for this user. They influence <<resources-user-profile,search results>>"
    const val ages = "The student ages taught by the user"
}

class UserDocTests : AbstractDocTests() {

    @Test
    fun `getting user profile`() {
        val responseFields = responseFields(
            fieldWithPath("id").description("The ID of the user"),
            fieldWithPath("firstName").description(Descriptions.firstName),
            fieldWithPath("lastName").description(Descriptions.lastName),
            fieldWithPath("ages").description(Descriptions.ages),
            subsectionWithPath("subjects").description(Descriptions.subjects),
            fieldWithPath("email").description("The email of the user"),
            subsectionWithPath("_links").description("HAL links related to this collection"),
            fieldWithPath("marketSegment").ignored()
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
        val subject = subjectsClient.getAllSubjects()._embedded.subjects[0].id
        val myLinks = getLinksFor(teacherAccessToken)

        val requestFields = requestFields(
            fieldWithPath("firstName").optional().description(Descriptions.firstName),
            fieldWithPath("lastName").optional().description(Descriptions.lastName),
            fieldWithPath("subjects").optional().description(Descriptions.subjects),
            fieldWithPath("ages").optional().description(Descriptions.ages),
            fieldWithPath("country").optional()
                .description("The country of the user (3-letter ISO Country Code)"),
            fieldWithPath("state").optional().description("The US state of the user (2-letter ISO Code)")
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
                    "subjects": ["$subject"],
                    "ages": [7,8,9],
                    "country": "USA",
                    "state": "AZ"
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
        stubOwnerSpec = RequestSpecificationFactory.createFor(teacherAccessToken, restDocumentation)
        links = getLinksFor(teacherAccessToken)
    }
}
