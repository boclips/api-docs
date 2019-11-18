package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
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
                        fieldWithPath("subjects").description("A list subjects that the user teaches"),
                        fieldWithPath("email").description("The email of the user"),
                        fieldWithPath("analyticsId").ignored(),
                        fieldWithPath("organisationAccountId").ignored(),
                        fieldWithPath("organisation").ignored(),
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
}
