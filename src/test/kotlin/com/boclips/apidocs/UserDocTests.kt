package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
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
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class UserDocTests : AbstractDocTests() {
    @Test
    fun `getting user profile`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-user-get-user-profile",
                    responseFields(
                        fieldWithPath("id").description("The ID of the user"),
                        fieldWithPath("firstName").description("The first name of the user"),
                        fieldWithPath("lastName").description("The last name of the user"),
                        fieldWithPath("ages").description("A list of ages that the user teaches"),
                        subsectionWithPath("subjects").description("A list subjects that the user teaches"),
                        fieldWithPath("email").description("The email of the user"),
                        fieldWithPath("analyticsId").ignored(),
                        fieldWithPath("organisationAccountId").ignored(),
                        subsectionWithPath("organisation").ignored(),
                        subsectionWithPath("_links").description("HAL links related to this collection")
                    ),
                    links(
                        linkWithRel("self").description("Points to this user profile"),
                        linkWithRel("profile").ignored()
                    )
                )
            )
            .`when`()
            .get(links["profile"])
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `updating user profile`(restDocumentation: RestDocumentationContextProvider) {
        val subject = videoServiceClient.subjects[0].id.value
        val myLinks = getLinksFor(updatableClientAccessToken)

        given(documentationSpec)
            .filter(
                document(
                    "resource-user-put-user-profile",
                    requestFields(
                        fieldWithPath("firstName").optional().description("The user's first name"),
                        fieldWithPath("lastName").optional().description("The user's last name"),
                        fieldWithPath("subjects").optional().description("The subjects of the user, by name"),
                        fieldWithPath("ages").optional().description("The ages taught by the user"),
                        fieldWithPath("country").optional().description("The country of the user (3-letter ISO Country Code)"),
                        fieldWithPath("state").optional().description("The state of the user (2-letter, US only)")
                    )
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
            .put(myLinks["profile"])
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @BeforeEach
    override fun setUp(restDocumentation: RestDocumentationContextProvider) {
        super.setUp(restDocumentation)
        documentationSpec = RequestSpecificationFactory.createFor(updatableClientAccessToken, restDocumentation)
        links = getLinksFor(updatableClientAccessToken)
    }
}
