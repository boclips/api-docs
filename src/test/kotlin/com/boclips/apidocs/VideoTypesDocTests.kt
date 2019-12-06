package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class VideoTypesDocTests : AbstractDocTests() {
    @Test
    fun `lists available video types`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-video-types",
                    responseFields(
                        fieldWithPath("_embedded.videoTypes").description("Video types available in the system")
                    )
                )
            )
            .`when`()
            .get(links["videoTypes"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}