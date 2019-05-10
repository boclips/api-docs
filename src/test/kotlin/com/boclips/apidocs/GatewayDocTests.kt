package com.boclips.apidocs

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class GatewayDocTests: AbstractDocTests() {

    @Test
    fun `resource index contains root links`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-index"
                , links(
                linkWithRel("video").description("A video resource, templated link to retrieve an individual video."),
                linkWithRel("search").description("Templated link to perform video search"),

                linkWithRel("createCollection").description("Link to create new video collections"),
                linkWithRel("myCollections").description("Collections created by the current user"),
                linkWithRel("publicCollections").description("Collections marked as public"),
                linkWithRel("bookmarkedCollections").description("Collections bookmarked by the current user"),
                linkWithRel("collection").description("Individual collection resource, templated link to retrieve an individual video collection"),

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
