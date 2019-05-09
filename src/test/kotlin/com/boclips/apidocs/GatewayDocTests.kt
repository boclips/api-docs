package com.boclips.apidocs

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class GatewayDocTests: AbstractDocTests() {

    @Test
    fun `resource index contains root links`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-index"
//                , links(
//                linkWithRel("video").description("A video resource")
//            )
                )
            )
            .`when`().get("/").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }
}
