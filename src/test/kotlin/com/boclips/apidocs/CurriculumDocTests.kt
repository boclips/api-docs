package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class CurriculumDocTests : AbstractDocTests() {

    @Test
    fun `get all ngss codes`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded[].code").description("NGSS code value"),
            fieldWithPath("_embedded[].contentArea").description("Content area associated to the NGSS code"),
            fieldWithPath("_embedded[].themaCategories").ignored(),
            fieldWithPath("_embedded[].themaCategories.defaults").ignored(),
            fieldWithPath("_embedded[].themaCategories.customByGrade.*.[]").ignored()
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-ngss-codes-all-get",
                    responseFields
                )
            ).filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get all NGSS codes",
                    false,
                    responseFields
                )
            )
            .`when`()
            .get(links["ngssCodes"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `get all ngss grades`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded[].grade").description("NGSS grade value"),
            fieldWithPath("_embedded[].description").description("Classes supported in the NGSS grade"),
            fieldWithPath("_embedded[].educationLevels").ignored()
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-ngss-grades-all-get",
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Get all NGSS grades",
                    false,
                    responseFields
                )
            )
            .`when`()
            .get(links["ngssGrades"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
