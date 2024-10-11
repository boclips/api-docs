package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.UriTemplateHelper.stripOptionalParameters
import com.boclips.videos.api.request.attachments.AttachmentRequest
import com.boclips.videos.api.request.collection.CreateCollectionRequest
import com.boclips.videos.api.request.collection.UpdateCollectionRequest
import com.boclips.videos.api.response.subject.SubjectResource
import com.damnhandy.uri.template.UriTemplate
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
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

class CollectionsDocTests : AbstractDocTests() {
    val aCollectionTitle = "Genetic Screening Debate"
    val collectionDesc =
        "Doctors and other health care professionals are faced with complex patient care issues as genetic testing becomes more widely available, study finds."

    @Test
    fun `adding a video to a collection`() {
        val pathParameters = pathParameters(
            parameterWithName("id").description("The ID of the collection"),
            parameterWithName("video_id").description("The ID of the video")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-collection-add-video",
                    pathParameters
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can add a video to a collection",
                    false,
                    pathParameters,
                )
            )
            .`when`()
            .put("/v1/collections/{id}/videos/{video_id}", aCollectionWithAttachments, someExistingVideoIds[0])
            .then()
            .assertThat().statusCode(`is`(HttpStatus.NO_CONTENT.value()))
    }

    @Test
    fun `removing a video from a collection`() {
        val pathParameters = pathParameters(
            parameterWithName("id").description("The ID of the collection"),
            parameterWithName("video_id").description("The ID of the video")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-collection-remove-video",
                    pathParameters
                )
            ).filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can remove a video from a collection",
                    false,
                    pathParameters,
                )
            )
            .`when`()
            .delete("/v1/collections/{id}/videos/{video_id}", aCollectionWithAttachments, someExistingVideoIds[0])
            .then()
            .assertThat().statusCode(`is`(HttpStatus.NO_CONTENT.value()))
    }

    @Test
    fun `creating a new collection`() {
        given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "resource-collection-creation",
                    "Allows creation of new collections",
                    false,
                    requestFields(
                        fieldWithPath("title")
                            .description("Collection's title"),
                        fieldWithPath("description")
                            .optional()
                            .description("Collection's description"),
                        fieldWithPath("videos")
                            .optional()
                            .description("A list of IDs of videos that should belong to this collection"),
                        fieldWithPath("subjects")
                            .optional()
                            .description("A list of IDs of subjects that should belong to this collection"),
                        fieldWithPath("discoverable")
                            .ignored()
                    )
                )
            )
            .`when`()
            .contentType(ContentType.JSON)
            .body(
                """
                {
                    "title": "$aCollectionTitle",
                    "description": "$collectionDesc",
                    "videos": ["${someExistingVideoIds[0]}", "${someExistingVideoIds[1]}"],
                    "subjects": [${subjects.joinToString(", ") { "\"${it.id}\"" }}],
                    "discoverable": true
                }
                """.trimIndent()
            )
            .post(links["createCollection"])
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(201))
    }

    @Test
    fun `retrieving a collection`() {
        testRetrievingCollection(snippetId = "Retrieving a collection")
    }

    @Test
    fun `retrieving a collection with detailed projection`() {
        testRetrievingCollection(
            snippetId = "Retrieving a collection with detailed projection",
            useDetailedProjection = true
        )
    }

    @Test
    fun `searching through collections`() {
        val requestParameters = requestParameters(
            parameterWithName("query")
                .optional()
                .description("A phrase you want to search by. Filters through collection titles")
                .attributes(
                    key("type").value("String")
                ),
            parameterWithName("discoverable")
                .optional()
                .description("By default only discoverable collections appear in search. To retrieve all collections use this property.")
                .attributes(
                    key("type").value("Boolean")
                ),
            parameterWithName("promoted")
                .optional()
                .description("Whether you want to search through promoted collections only or not")
                .attributes(
                    key("type").value("Boolean")
                ),
            parameterWithName("subject")
                .optional()
                .description("Allows to limit search results to specific subjects only")
                .attributes(
                    key("type").value("List of subject IDs")
                ),
            parameterWithName("age_range_min")
                .optional()
                .description("Minimum age to filter from - it filters on the collection age range property, and is inclusive")
                .attributes(
                    key("type").value("Number")
                ),
            parameterWithName("age_range_max")
                .optional()
                .description("Maximum age to filter to - it filters on the collection age range property, and is inclusive")
                .attributes(
                    key("type").value("Number")
                ),
            parameterWithName("age_range")
                .optional()
                .description("Filters on the video age ranges. Provide age ranges in the form `minAge-maxAge`, ie `5-7`. These ranges are inclusive.")
                .attributes(
                    key("type").value("String")
                ),
            parameterWithName("has_lesson_plans")
                .optional()
                .description("Allows to limit search results to collection with lesson plan attachment only")
                .attributes(
                    key("type").value("Boolean")
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
                .description("Controls how sub-resources are fetched. Allowed values are `list` for shallow details and `details` for full sub-resource information. See <<resources-collections-projections,here>> for more details")
                .attributes(
                    key("type").value("Integer")
                ),
            parameterWithName("sort_by")
                .optional()
                .description("Sort collections by UPDATED_AT (last updated collections appear first), IS_DEFAULT (Watch later collections appear first), HAS_ATTACHMENT (collections with attachments appear first)")
                .attributes(
                    key("type").value("UPDATED_AT, IS_DEFAULT, HAS_ATTACHMENT")
                )
        )
        val responseFields = responseFields(
            subsectionWithPath("_embedded.collections").description("Collection resources array. See <<resources-collections-retrieve_response_fields,collection>> for payload details"),
            *pageSpecificationResponseFields,
            subsectionWithPath("_links").description("HAL links for the collection resource")
        )
        given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "Collection Search",
                    "Collection search description",
                    false,
                    requestParameters,
                    responseFields,
                )
            )
            .filter(
                document(
                    "resource-collection-search",
                    requestParameters,
                    responseFields,
                    links(
                        linkWithRel("self").description("Points to this exact search query"),
                        linkWithRel("next").description("Points to next page of collections"),
                        linkWithRel("details").description("Points to this search query with details projection"),
                        linkWithRel("list").description("Points to this search query with list projection")
                    )
                )
            )
            .`when`()
            .get(
                UriTemplate.fromTemplate(links["searchCollections"])
                    .set("query", "collection")
                    .set("page", 0)
                    .set("size", 1)
                    .set("projection", "list")
                    .expand()
            )
            .apply { println(prettyPrint()) }
            .then()
            .assertThat()
            .statusCode(`is`(200))
    }

    @Test
    fun `editing collections`() {
        val requestFields = requestFields(
            fieldWithPath("title")
                .optional()
                .description("Collection's title"),
            fieldWithPath("description")
                .optional()
                .description("Collection's description"),
            fieldWithPath("videos")
                .optional()
                .description("A list of IDs of videos that should belong to this collection. Will replace existing videos"),
            fieldWithPath("subjects")
                .optional()
                .description("A list of IDs of subjects that should belong to this collection. Will replace existing subjects"),
            fieldWithPath("discoverable")
                .optional()
                .description("Whether the new collection is visible in search and has been vetted by Boclips"),
            fieldWithPath("ageRange.min")
                .optional()
                .description("The lower bound of age range this collection of videos is suitable for"),
            fieldWithPath("ageRange.max")
                .optional()
                .description("The upper bound of age range this collection of videos is suitable for"),
            subsectionWithPath("attachment")
                .optional()
                .description("An optional <<resources-collections-attachments,attachment>> that can be added to this collection"),
            fieldWithPath("attachment.linkToResource").description("A link that points to attachment's actual content"),
            fieldWithPath("attachment.type").type("String")
                .description("The type of the attachment. Currently we support `LESSON_PLAN` only"),
            fieldWithPath("attachment.description").optional().description("Text that describes the attachment")
        )
        given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "resource-collection-edit",
                    "Collection Edit description",
                    false,
                    requestFields,
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    ),
                )
            )
            .filter(
                document(
                    "resource-collection-edit",
                    requestFields,
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    ),
                )
            )
            .`when`()
            .contentType(ContentType.JSON)
            .body(
                """
                {
                    "title": "$aCollectionTitle",
                    "description": "$collectionDesc",
                    "videos": ["${someExistingVideoIds[0]}", "${someExistingVideoIds[1]}"],
                    "subjects": [${subjects.joinToString(", ") { "\"${it.id}\"" }}],
                    "discoverable": true,
                    "ageRange": {
                        "min": 8,
                        "max": 12
                    },
                    "attachment": {
                        "linkToResource": "https://docs.google.com/document/d/1SBf26k2PEPsChg2X4yv6F71uqp8bECcYFtAFSmTDN10/edit?usp=sharing",
                        "description": "1.Solving Problems with The Scientific Method: We Do it Everyday! 1.Plan A Science Fair Project",
                        "type": "LESSON_PLAN"
                    }
                }
                """.trimIndent()
            )
            .patch("/v1/collections/{id}", aCollectionWithAttachments)
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(HttpStatus.NO_CONTENT.value()))
    }

    @Test
    fun `bookmarking a collection`() {
        given(apiUserSpec)
            .filter(
                document(
                    "resource-collection-bookmark",
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    )
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "bookmark a collection",
                    "Collection bookmark description",
                    false,
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    )
                )
            )
            .`when`()
            .queryParam("bookmarked", true)
            .patch("/v1/collections/{id}", aCollection)
            .then()
            .assertThat().statusCode(`is`(HttpStatus.OK.value()))
            .and()
            .body("id", equalTo(aCollection))
    }

    @BeforeEach
    fun setupTestData() {
        subjects = subjectsClient.getAllSubjects()._embedded.subjects.take(2)

        aCollectionWithAttachments = collectionsClient.create(
            CreateCollectionRequest(
                title = aCollectionTitle,
                description = collectionDesc,
                videos = listOf("5c542abf5438cdbcb56df0bf"),
                subjects = subjects.map { it.id }.toSet(),
                discoverable = true
            )
        ).id!!

        collectionsClient.update(
            collectionId = aCollectionWithAttachments,
            update = UpdateCollectionRequest(
                attachment = AttachmentRequest(
                    linkToResource = "https://docs.google.com/document/d/1SBf26k2PEPsChg2X4yv6F71uqp8bECcYFtAFSmTDN10/edit?usp=sharing",
                    description = "1.Solving Problems with The Scientific Method: We Do it Everyday! 1.Plan A Science Fair Project",
                    type = "LESSON_PLAN"
                )
            )
        )

        aCollection = collectionsClient.create(
            CreateCollectionRequest(
                title = anotherCollectionTitle,
                description = "This content is accessible by everyone",
                videos = listOf(someExistingVideoIds[0], someExistingVideoIds[1]),
                subjects = subjects.map { it.id }.toSet(),
                discoverable = true
            )
        ).id!!
    }

    @AfterEach
    fun tearDownTestData() {
        try {
            collectionsClient.delete(aCollection)
            collectionsClient.delete(aCollectionWithAttachments)
        } catch (ex: Exception) {
        }
    }

    private fun testRetrievingCollection(snippetId: String, useDetailedProjection: Boolean = false) {
        val responseFields = responseFields(
            fieldWithPath("id").description("The ID of the collection"),
            fieldWithPath("owner").description("The ID of the collection's owner"),
            fieldWithPath("ownerName").description("The name of the collection's owner"),
            fieldWithPath("title").description("Collection's title"),
            fieldWithPath("description").description("Collection's description"),
            subsectionWithPath("videos").description("A list of <<resources-videos,videos>> in the collection. Shallow video details are returned by default"),
            subsectionWithPath("subjects").description("A list of subjects assigned to this collection. See <<resources-subjects_response_fields,subjects>> for payload details"),
            fieldWithPath("updatedAt").description("A timestamp of collection's last update"),
            fieldWithPath("public").ignored(),
            fieldWithPath("discoverable").description("Discoverable collections are discoverable through searching and browsing."),
            fieldWithPath("promoted").description("Whether the collection is promoted"),
            fieldWithPath("promotedFor").ignored(),
            fieldWithPath("mine").description("Whether the collection belongs to me"),
            fieldWithPath("createdBy").description("Name of collection's creator"),
            fieldWithPath("subjects").description("A list of teaching subjects this collection relates to"),
            fieldWithPath("ageRange").description("Tells which ages videos in this collection are suitable for"),
            subsectionWithPath("subCollections").ignored(),
            subsectionWithPath("permissions").ignored(),
            subsectionWithPath("attachments").description("A list of <<resources-collections-attachments,attachments>> linked to this collection"),
            subsectionWithPath("segments").ignored(),
            subsectionWithPath("videoNotes").ignored(),
            subsectionWithPath("_links").description("HAL links related to this collection"),
            fieldWithPath("attachments[*].id").description("ID of the attachment"),
            fieldWithPath("attachments[*].type").description("The type of the attachment: `LESSON_PLAN` `ACTIVITY`")
                .type("String"),
            fieldWithPath("attachments[*].description").description("Text that describes the attachment"),
            fieldWithPath("attachments[*]._links.download.href").description("A link that points to attachment's actual content"),
            fieldWithPath("attachments[*]._links.download.templated").ignored()
        )
        val resourceLinks = links(
            linkWithRel("self").description("Points to this collection"),
            linkWithRel("edit").description("`PATCH` requests can be sent to this URL to update the collection"),
            linkWithRel("safeEdit").ignored(),
            linkWithRel("remove").description("`DELETE` request can be sent to this URL to remove the collection (videos remain in the system)"),
            linkWithRel("addVideo").description("`PUT` requests to this URL allow to add more videos to this collection"),
            linkWithRel("removeVideo").description("`DELETE` requests to this URL allow to remove videos from this collection"),
            linkWithRel("interactedWith").description("`POST` requests to this URL to track collection interaction events"),
            linkWithRel("updatePermissions").description("`PATCH` requests to this URL allow to update collection permissions")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    snippetId,
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    ),
                    responseFields,
                    resourceLinks
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    snippetId, "Retrieving a collection by ID", false,
                    pathParameters(
                        parameterWithName("id").description("The ID of the collection")
                    ),
                    responseFields, resourceLinks
                )
            )
            .`when`()
            .apply {
                if (useDetailedProjection) {
                    queryParam("projection", "details")
                }
            }
            .get(stripOptionalParameters(links["collection"]), aCollectionWithAttachments)
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    val someExistingVideoIds = listOf("5c542abf5438cdbcb56df0bf", "5cf15aaece7c2c4e212747d3")
    val anotherCollectionTitle = "Discoverable Boclips Collection"

    lateinit var subjects: List<SubjectResource>
    lateinit var aCollectionWithAttachments: String
    lateinit var aCollection: String
}
