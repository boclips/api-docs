package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class EducationLevelsDocTests : AbstractDocTests() {
    @Test
    fun `return all education levels`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.levels[].code").description("Code of the education level, can be used for filtering"),
            fieldWithPath("_embedded.levels[].label").description("Human readable education level name"),
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-education-levels-all",
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get all education levels",
                    false,
                    responseFields
                )
            )
            .`when`()
            .get(links["educationLevels"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
