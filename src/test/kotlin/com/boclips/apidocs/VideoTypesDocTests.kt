package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class VideoTypesDocTests : AbstractDocTests() {
    @Test
    fun `List available video types`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.videoTypes").description("Video types available in the system")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-video-types",
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "List all available video types",
                    false,
                    responseFields
                )
            )
            .`when`()
            .get(links["videoTypes"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
