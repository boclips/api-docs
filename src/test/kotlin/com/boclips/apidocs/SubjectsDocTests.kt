package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
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
    fun `resource index contains root links`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-subjects",
                    responseFields(
                        fieldWithPath("_embedded.subjects[].id").description("Id of the subject, can be used for filtering"),
                        fieldWithPath("_embedded.subjects[].name").description("Human readable subject name"),
                        subsectionWithPath("_embedded.subjects[]._links").description("HAL links for the subject resource"),
                        subsectionWithPath("_links").description("HAL links for the subject collection resource")
                    ), links(
                        linkWithRel("self").description("The subject collection resource that was just retrieved")
                    )
                )
            )
            .`when`()
            .get(links["subjects"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
