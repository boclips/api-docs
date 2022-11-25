package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class CurriculumDocTests : AbstractDocTests() {

    @Test
    fun `get all ngss codes`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-ngss-codes-all-get",
                    responseFields(
                        fieldWithPath("_embedded[].code").description("NGSS code value"),
                        fieldWithPath("_embedded[].contentArea").description("Content area associated to the NGSS code")
                        fieldWithPath("_embedded[].themaCategories").description("Thema category of the NGSS code")
                    )
                )
            )
            .`when`()
            .get(links["ngssCodes"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `get all ngss grades`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-ngss-grades-all-get",
                    responseFields(
                        fieldWithPath("_embedded[].grade").description("NGSS grade value"),
                        fieldWithPath("_embedded[].description").description("Classes supported in the NGSS grade")
                        fieldWithPath("_embedded[].educationalLevel").description("Educational grade of NGSS grade")
                    )
                )
            )
            .`when`()
            .get(links["ngssGrades"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
