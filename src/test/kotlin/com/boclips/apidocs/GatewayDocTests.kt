package com.boclips.apidocs

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import javax.servlet.RequestDispatcher



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
    fun `resource index contains root links`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-index"
                , links(
                linkWithRel("video").description("The video resource, templated link to retrieve an individual video"),
                linkWithRel("searchVideos").description("Templated link to perform video search"),

                linkWithRel("collection").description("The collection resource, templated link to retrieve an individual video collection"),
                linkWithRel("createCollection").description("Link to create a new video collection"),
                linkWithRel("myCollections").description("Collections created by the current user"),
                linkWithRel("publicCollections").description("Collections marked as public"),
                linkWithRel("searchCollections").description("Searchable collections"),
                linkWithRel("bookmarkedCollections").description("Collections bookmarked by the current user"),

                linkWithRel("subjects").description("List of subjects available"),
                linkWithRel("profile").description("User profile information"),

                linkWithRel("createNoSearchResultsEvent").description("Deprecated"),
                linkWithRel("createPlaybackEvent").description("Deprecated")
            )
                )
            )
            .`when`().get("/").apply { prettyPrint() }
            .then().assertThat().statusCode(`is`(200))
    }
}
