package com.boclips.apidocs

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class CollectionsDocTest : AbstractDocTests() {
    @Test
    fun `creating a new collection`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-collection-creation",
                    requestFields(
                        fieldWithPath("title")
                            .description("Collection's title"),
                        fieldWithPath("description")
                            .optional()
                            .description("Collection's description"),
                        fieldWithPath("videos")
                            .optional()
                            .description("A list of IDs of videos that should belong to this collection"),
                        fieldWithPath("public")
                            .optional()
                            .description("Whether the new collection should be visible only to you or to everyone")
                    )
                )
            )
            .body(
                """
                {
                    "title": "Life at Boclips",
                    "description": "Working hard on them APIs",
                    "videos": ["5c542abf5438cdbcb56df0bf", "5cf15aaece7c2c4e212747d3"],
                    "public": true
                }
            """.trimIndent()
            )
            .contentType(ContentType.JSON)
            .`when`()
            .post("/collections")
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(201))
    }
}