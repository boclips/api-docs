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
                    )
                )
            )
            .`when`()
            .get(links["ngssGrades"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `get all openstax books`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-curriculum-openstax-books-all-get",
                    responseFields(
                        fieldWithPath("_embedded.books[].title").description("Title of the book"),
                        fieldWithPath("_embedded.books[].id").description("Id of the book"),
                        fieldWithPath("_embedded.books[].chapters[].number").description("Number of a chapter"),
                        fieldWithPath("_embedded.books[].chapters[].title").description("Title of a chapter"),
                        fieldWithPath("_embedded.books[].chapters[].sections[]").description("Sections of a chapter"),
                        fieldWithPath("_embedded.books[].chapters[].sections[].number").description("An optional section number")
                            .optional(),
                        fieldWithPath("_embedded.books[].chapters[].sections[].title").description("Title of a section")
                            .optional(),
                    )
                )
            )
            .`when`()
            .get(links["openstaxBooks"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
