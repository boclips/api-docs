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
    fun `get all curriculum`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-ngss-codes-all-get",
                    responseFields(
                        fieldWithPath("_embedded[].code").description("NGSS code value"),
                        fieldWithPath("_embedded[].contentArea").description("Content area associated to the NGSS code")
                    )
                )
            )
            .`when`()
            .get(links["ngssCodes"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
