package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
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
        given(stubOwnerSpec)
            .filter(
                document(
                    "error-example",
                    responseFields(
                        fieldWithPath("error").description("The HTTP error that occurred, e.g. `Invalid field`"),
                        fieldWithPath("message").description("A description of the cause of the error"),
                        fieldWithPath("reasons").description("The list of reasons which caused error"),
                        fieldWithPath("path").description("The path to which the request was made"),
                        fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                        fieldWithPath("timestamp").description("The time at which the error occurred")
                    )
                )
            )
            .`when`().get("/v1/videos?duration_min=not-quite-a-number").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(400))
    }

    @Test
    fun `resource index contains root links`(restDocumentation: RestDocumentationContextProvider) {
        val indexDocumentationSpec = RequestSpecificationFactory.createFor(freshClientAccessToken, restDocumentation)

        val links = links(
            linkWithRel("trackPageRendered").description("`POST` endpoint for tracking pageRendered event"),
            linkWithRel("trackPlatformInteractedWith").description("`POST` endpoint for tracking a platform interaction event"),
            linkWithRel("createPlaybackEvents").description("Sending <<_sending_a_batch_of_playback_events,batches>> of playback events from the past"),
            linkWithRel("createSearchQueryCompletionsSuggestedEvent").description("`POST` endpoint for tracking search completions suggested event"),

            linkWithRel("activate").description("A `PUT` request against this link allows to pass profile information and activate a new user"),
            linkWithRel("profile").description("Templated link to get user profile information"),
            linkWithRel("currentUser").description("Get the current user's profile"),

            linkWithRel("video").description("The video resource, templated link to retrieve an individual video"),
            linkWithRel("searchVideos").description("Templated link to perform video search"),
            linkWithRel("videoFeed").description("A feed of videos for deep pagination"),

            linkWithRel("videoTypes").description("Lists <<resources-video-types,types>> of videos available in the system. These can be later used when <<resources-video-search,searching>>"),

            linkWithRel("tags").description("List of tags that can be attached to videos"),

            linkWithRel("createCollection").description("Link to create a new video collection"),
            linkWithRel("myCollections").description("Collections created by the current user"),
            linkWithRel("mySavedCollections").description("Collections created or bookmarked by the current user"),
            linkWithRel("discoverCollections").description("Collections that have been curated by Boclips and are considered a great starting point for exploration."),
            linkWithRel("promotedCollections").description("Collections that are promoted, e.g. on a homepage."),
            linkWithRel("searchCollections").description("Search all collections"),
            linkWithRel("collection").description("The collection resource, templated link to retrieve an individual video collection"),

            linkWithRel("subjects").description("List of subjects available that will return videos"),
            linkWithRel("allSubjects").description("List of all subjects available in the system"),

            linkWithRel("educationLevels").description("List of available education levels"),

            linkWithRel("disciplines").description("List of disciplines that will return videos (e.g. arts, humanities...)"),
            linkWithRel("allDisciplines").description("List of all disciplines available in the system (e.g. arts, humanities...)"),

            linkWithRel("ngssCodes").description("List of all NGSS codes available"),
            linkWithRel("ngssGrades").description("List of all NGSS grades available"),

            linkWithRel("countries").description("List of countries"),

            linkWithRel("channel").description("Retrieve a specific channel"),
            linkWithRel("channels").description("Retrieve all channels"),
            linkWithRel("contentCategories").description("Retrieve a list of content categories"),
            linkWithRel("validateShareCode").description("Validate a share code for a given user"),
            linkWithRel("isUserActive").description("Check whether given user is active"),
            linkWithRel("contractLegalRestrictions").ignored(),
            linkWithRel("suggestions").ignored(),
            linkWithRel("getMetadata").ignored(),
            linkWithRel("getAllProviders").description("List all providers and their types"),
            linkWithRel("getThemesByProviderAndId").ignored(),
            linkWithRel("getThemesByIds").ignored(),
            linkWithRel("getThemesByProvider").description("List all available theme for a specific provider"),
            linkWithRel("access").ignored(),
            linkWithRel("license").ignored(),
            linkWithRel("learningOutcomes").ignored(),
            linkWithRel("assessmentQuestions").ignored(),
            linkWithRel("getAccount").ignored(),
        )
        given(indexDocumentationSpec)
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}", "Links fetching", false, links

                )
            )
            .filter(
                document(
                    "resource-index",
                    links
                )
            )
            .`when`().get("/v1/").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }
}
