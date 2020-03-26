package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class GatewayDocTests : AbstractDocTests() {

    @Test
    fun `error example`() {
        given(documentationSpec)
            .filter(
                document(
                    "error-example"
                    , responseFields(
                        fieldWithPath("error").description("The HTTP error that occurred, e.g. `Invalid field`"),
                        fieldWithPath("message").description("A description of the cause of the error"),
                        fieldWithPath("path").description("The path to which the request was made"),
                        fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                        fieldWithPath("timestamp").description("The time at which the error occurred")
                    )
                )
            )
            .`when`().get("/videos?duration_min=not-quite-a-number").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(400))
    }

    @Test
    fun `resource index contains root links`(restDocumentation: RestDocumentationContextProvider) {
        val indexDocumentationSpec = RequestSpecificationFactory.createFor(freshClientAccessToken, restDocumentation)

        given(indexDocumentationSpec)
            .filter(
                document(
                    "resource-index"
                    , links(
                        linkWithRel("trackPageRendered").description("`POST` endpoint for tracking pageRendered event"),
                        linkWithRel("createPlaybackEvents").description("Sending <<_sending_a_batch_of_playback_events,batches>> of playback events from the past"),

                        linkWithRel("activate").description("A `PUT` request against this link allows to pass profile information and activate a new user"),
                        linkWithRel("profile").description("Templated link to get user profile information"),

                        linkWithRel("video").description("The video resource, templated link to retrieve an individual video"),
                        linkWithRel("searchVideos").description("Templated link to perform video search"),

                        linkWithRel("videoTypes").description("Lists <<resources-video-types,types>> of videos available in the system. These can be later used when <<resources-video-search,searching>>"),

                        linkWithRel("tags").description("List of tags that can be attached to videos"),

                        linkWithRel("collection").description("The collection resource, templated link to retrieve an individual video collection"),
                        linkWithRel("createCollection").description("Link to create a new video collection"),
                        linkWithRel("myCollections").description("Collections created by the current user"),
                        linkWithRel("mySavedCollections").description("Collections created or bookmarked by the current user"),
                        linkWithRel("publicCollections").description("Collections marked as public"),
                        linkWithRel("promotedCollections").description("Collections that are promoted"),
                        linkWithRel("searchPublicCollections").description("Search all public collections"),
                        linkWithRel("searchCollections").description("Search all collections"),
                        linkWithRel("bookmarkedCollections").description("Collections bookmarked by the current user"),

                        linkWithRel("subjects").description("List of subjects available"),

                        linkWithRel("disciplines").description("List of disciplines available in the system (e.g. arts, humanities...)"),

                        linkWithRel("countries").description("List of countries"),

                        linkWithRel("contentPartner").description("Retrieve a specific content partner"),
                        linkWithRel("contentPartners").description("Retrieve all content partners"),
                        linkWithRel("contentCategories").description("Retrieve a list of content partner categories"),
                        linkWithRel("validateShareCode").description("Validate a share code for a given user")
                    )
                )
            )
            .`when`().get("/").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }
}
