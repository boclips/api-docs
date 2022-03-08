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

class DisciplineDocTests : AbstractDocTests() {
    @Test
    fun `get user specific disciplines`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-disciplines-user-get",
                    responseFields(
                        fieldWithPath("_embedded.disciplines[].id").description("Id of the discipline"),
                        fieldWithPath("_embedded.disciplines[].name").description("Name of the discipline"),
                        fieldWithPath("_embedded.disciplines[].code").description("kebab-case version of the name"),
                        subsectionWithPath("_embedded.disciplines[]._links")
                            .description("HAL links for the individual disciplines"),
                        subsectionWithPath("_embedded.disciplines[].subjects")
                            .description("A list of <<resources-subjects, subjects>> associated to this discipline"),
                        subsectionWithPath("_links").description("HAL links for the discipline collection resource")
                    ),
                    links(linkWithRel("self").description("The discipline resource that was just retrieved"))
                )
            )
            .`when`()
            .get(links["disciplines"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `get all disciplines`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-disciplines-all-get",
                    responseFields(
                        fieldWithPath("_embedded.disciplines[].id").description("Id of the discipline"),
                        fieldWithPath("_embedded.disciplines[].name").description("Name of the discipline"),
                        fieldWithPath("_embedded.disciplines[].code").description("kebab-case version of the name"),
                        subsectionWithPath("_embedded.disciplines[]._links")
                            .description("HAL links for the individual disciplines"),
                        subsectionWithPath("_embedded.disciplines[].subjects")
                            .description("A list of <<resources-subjects, subjects>> associated to this discipline"),
                        subsectionWithPath("_links").description("HAL links for the discipline collection resource")
                    ),
                    links(linkWithRel("self").description("The discipline resource that was just retrieved"))
                )
            )
            .`when`()
            .get(links["disciplines"] + "?visibility=all").apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
