package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

class GatewayDocTests: AbstractDocTests() {

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
                                fieldWithPath("timestamp").description("The time at which the error occurred"))
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
                linkWithRel("activate").description("A `PUT` request against this link allows to pass profile information and activate a new user"),
                linkWithRel("trackPageRendered").description("..."),

                linkWithRel("video").description("The video resource, templated link to retrieve an individual video"),
                linkWithRel("searchVideos").description("Templated link to perform video search"),

                linkWithRel("trackPageRendered").description("`POST` endpoint for tracking pageRendered event"),

                linkWithRel("collection").description("The collection resource, templated link to retrieve an individual video collection"),
                linkWithRel("createCollection").description("Link to create a new video collection"),
                linkWithRel("myCollections").description("Collections created by the current user"),
                linkWithRel("publicCollections").description("Collections marked as public"),
                linkWithRel("searchPublicCollections").description("Search all public collections"),
                linkWithRel("searchCollections").description("Search all collections"),
                linkWithRel("bookmarkedCollections").description("Collections bookmarked by the current user"),

                linkWithRel("contentPartner").description("Retrieve a specific content partner"),
                linkWithRel("contentPartners").description("Retrieve all content partners"),

                linkWithRel("subjects").description("List of subjects available"),
                linkWithRel("profile").description("Templated link to get user profile information"),
                linkWithRel("countries").description("List of countries"),

                linkWithRel("tags").description("List of tags that can be attached to videos"),

                linkWithRel("disciplines").description("List of disciplines available in the system (e.g. arts, humanities...)")
            )
                )
            )
            .`when`().get("/").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }
}
