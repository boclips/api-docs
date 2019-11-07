package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.videos.service.client.CreateCollectionRequest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
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

    @Test
    fun `retrieving a collection`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-collection",
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    ),
                    responseFields(
                        fieldWithPath("id").description("The ID of the collection"),
                        fieldWithPath("owner").description("The ID of the collection's owner"),
                        fieldWithPath("title").description("Collection's title"),
                        fieldWithPath("description").description("Collection's description"),
                        subsectionWithPath("videos").description("A list of videos in the collection. Shallow video details are returned by default"),
                        fieldWithPath("updatedAt").description("A timestamp of collection's last update"),
                        fieldWithPath("public").description("Whether the collection is publicly available"),
                        fieldWithPath("mine").description("Whether the collection belongs to me"),
                        fieldWithPath("createdBy").description("Name of collection's creator"),
                        fieldWithPath("subjects").description("A list of teaching subjects this collection relates to"),
                        fieldWithPath("ageRange").description("Tells which ages videos in this collection are suitable for"),
                        fieldWithPath("attachments").description("A list of items attached to this collection"),
                        subsectionWithPath("_links").description("HAL links related to this collection")
                    ),
                    links(
                        linkWithRel("self").description("Points to this collection"),
                        linkWithRel("edit").description("`PATCH` requests can be sent to this URL to update the collection"),
                        linkWithRel("remove").description("`DELETE` request can be sent to this URL to remove the collection (videos remain in the system)"),
                        linkWithRel("addVideo").description("`PUT` requests to this URL allow to add more videos to this collection"),
                        linkWithRel("removeVideo").description("`DELETE` requests to this URL allow to remove videos from this collection")
                    )
                )
            )
            .`when`()
            .get("/collections/{id}", collectionId).apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @BeforeEach
    fun createTestCollection() {
        collectionId = videoServiceClient.createCollection(
            CreateCollectionRequest.builder()
                .title("Life at Boclips")
                .description("Working hard on them APIs")
                .videos(listOf("5c542abf5438cdbcb56df0bf"))
                .isPublic(true)
                .build()
        ).uri.path.substringAfterLast("/")
    }

    lateinit var collectionId: String
}