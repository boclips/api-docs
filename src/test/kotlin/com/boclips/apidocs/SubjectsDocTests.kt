package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class SubjectsDocTests : AbstractDocTests() {
    @Test
    fun `returns user specific subjects`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.subjects[].id").description("Id of the subject, can be used for filtering"),
            fieldWithPath("_embedded.subjects[].name").description("Human readable subject name"),
            fieldWithPath("_embedded.subjects[].categories").description("Thema subject categories"),
            subsectionWithPath("_embedded.subjects[]._links").description("HAL links for the subject resource"),
            subsectionWithPath("_links").description("HAL links for the subject collection resource")
        )
        val responseLinks = links(
            linkWithRel("self").description("The subject collection resource that was just retrieved")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-subjects-user",
                    responseFields,
                    responseLinks
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get all available subjects for the user",
                    false,
                    responseFields,
                    responseLinks
                )
            )
            .`when`()
            .get(links["subjects"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `returns all subjects`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.subjects[].id").description("Id of the subject, can be used for filtering"),
            fieldWithPath("_embedded.subjects[].name").description("Human readable subject name"),
            fieldWithPath("_embedded.subjects[].categories").description("Thema subject categories"),
            subsectionWithPath("_embedded.subjects[]._links").description("HAL links for the subject resource"),
            subsectionWithPath("_links").description("HAL links for the subject collection resource")
        )
        val responseLinks = links(
            linkWithRel("self").description("The subject collection resource that was just retrieved")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-subjects-all",
                    responseFields,
                    responseLinks
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get all subjects",
                    false,
                    responseFields,
                    responseLinks
                )
            )
            .`when`()
            .get(links["subjects"] + "?visibility=all").apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
