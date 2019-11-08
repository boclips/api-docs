package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.videos.service.client.CreateCollectionRequest
import com.boclips.videos.service.client.Subject
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
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes.key

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
        testRetrievingCollection(snippetId = "resource-collection")
    }

    @Test
    fun `retrieving a collection with detailed projection`() {
        testRetrievingCollection(
            snippetId = "resource-collection-detailed",
            useDetailedProjection = true
        )
    }

    @Test
    fun `searching through collections`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-collection-search",
                    requestParameters(
                        parameterWithName("query")
                            .optional()
                            .description("A phrase you want to search by. Filters through collection titles")
                            .attributes(
                                key("type").value("String")
                            ),
                        parameterWithName("public")
                            .optional()
                            .description("Whether you want to search through public collections only or not")
                            .attributes(
                                key("type").value("Boolean")
                            ),
                        parameterWithName("subject")
                            .optional()
                            .description("Allows to limit search results to specific subjects only")
                            .attributes(
                                key("type").value("List of subject IDs")
                            ),
                        parameterWithName("page")
                            .optional()
                            .description("Index of search results page to retrieve")
                            .attributes(
                                key("type").value("Integer")
                            ),
                        parameterWithName("size")
                            .optional()
                            .description("Collection page size")
                            .attributes(
                                key("type").value("Integer")
                            ),
                        parameterWithName("projection")
                            .optional()
                            .description("Controls how sub-resources are fetched. Allowed values are `list` for shallow details and `details` for full sub-resource information")
                            .attributes(
                                key("type").value("Integer")
                            )
                    ),
                    responseFields(
                        subsectionWithPath("_embedded.collections").description("Collection resources array. See <<resources-collections-retrieve_response_fields,collection>> for payload details"),
                        *pageSpecificationResponseFields,
                        subsectionWithPath("_links").description("HAL links for the collection resource")
                    ),
                    links(
                        linkWithRel("self").description("Points to this exact search query"),
                        linkWithRel("next").description("Points to next page of collections"),
                        linkWithRel("details").description("Points to this search query with details projection"),
                        linkWithRel("list").description("Points to this search query with list projection")
                    )
                )
            )
            .`when`()
            .queryParam("query", "boclips")
            .queryParam("public", true)
            .queryParam("subject", subjects.map { it.id.value })
            .queryParam("page", 0)
            .queryParam("size", 30)
            .queryParam("projection", "list")
            .get("/collections").apply { println(prettyPrint()) }
            .then()
            .assertThat()
            .statusCode(`is`(200))
    }

    @BeforeEach
    fun createTestCollection() {
        val retrievedSubjects = videoServiceClient.subjects
        subjects = listOf(retrievedSubjects.component1(), retrievedSubjects.component2())

        collectionId = videoServiceClient.createCollection(
            CreateCollectionRequest.builder()
                .title("Life at Boclips")
                .description("Working hard on them APIs")
                .videos(listOf("5c542abf5438cdbcb56df0bf"))
                .subjects(subjects.map { it.id.value }.toSet())
                .isPublic(true)
                .build()
        ).uri.path.substringAfterLast("/")
    }

    lateinit var subjects: List<Subject>
    lateinit var collectionId: String

    fun testRetrievingCollection(snippetId: String, useDetailedProjection: Boolean = false) {
        given(documentationSpec)
            .filter(
                document(
                    snippetId,
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    ),
                    responseFields(
                        fieldWithPath("id").description("The ID of the collection"),
                        fieldWithPath("owner").description("The ID of the collection's owner"),
                        fieldWithPath("title").description("Collection's title"),
                        fieldWithPath("description").description("Collection's description"),
                        subsectionWithPath("videos").description("A list of videos in the collection. Shallow video details are returned by default"),
                        subsectionWithPath("subjects").description("A list of subjects assigned to this collection. See <<resources-subjects_response_fields,subjects>> for payload details"),
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
            .apply {
                if (useDetailedProjection) {
                    queryParam("projection", "details")
                }
            }
            .get("/collections/{id}", collectionId).apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}