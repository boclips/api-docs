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

class CurriculumDocTests : AbstractDocTests() {

    @Test
    fun `get all curriculum`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-all-get",
                    responseFields(
                        fieldWithPath("_embedded[].code").description("Id of the discipline"),
                        fieldWithPath("_embedded[].contentArea").description("Name of the discipline")
                    )
                )
            )
            .`when`()
            .get(links["curriculum"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
