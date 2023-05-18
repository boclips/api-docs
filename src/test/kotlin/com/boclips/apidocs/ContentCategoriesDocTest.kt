package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class ContentCategoriesDocTest : AbstractDocTests() {
    @Test
    fun `List available video types`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.contentCategories").description("List of categories that channel's content can be described as."),
            fieldWithPath("_embedded.contentCategories[*].label").ignored(),
            fieldWithPath("_embedded.contentCategories[*].key").ignored()
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resources-channels",
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get all available video types",
                    false,
                    responseFields
                )
            )
            .`when`()
            .get(links["contentCategories"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
