package com.boclips.apidocs
import com.boclips.apidocs.testsupport.AbstractDocTests
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class ContentCategories : AbstractDocTests()  {
    @Test
    fun `lists available video types`() {
        given(documentationSpec)
            .filter(
                document(
                    "resources-content-partners",
                    responseFields(
                        fieldWithPath("_embedded.contentCategories").description("List of categories that content partner content can be described as."),
                        fieldWithPath("_embedded.contentCategories[*].label").ignored(),
                        fieldWithPath("_embedded.contentCategories[*].key").ignored()
                    )
                )
            )
            .`when`()
            .get(links["contentCategories"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}



