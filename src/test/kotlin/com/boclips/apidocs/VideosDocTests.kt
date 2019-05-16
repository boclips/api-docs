package com.boclips.apidocs

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class VideosDocTests : AbstractDocTests() {

    @Test
    fun `video details`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-video"
                    , pathParameters(
                        parameterWithName("id").description("The ID of the video asset")
                    )
                    , PayloadDocumentation.responseFields(
                        fieldWithPath("id").description("The unique identifier for this video, can be interpolated in templated links"),
                        fieldWithPath("title").description("Human readable title for this video"),
                        fieldWithPath("description").description("Description detailing what this video talks about"),
                        fieldWithPath("releasedOn").description("Date on which the video was originally released as stated by the content producer"),
                        fieldWithPath("subjects").description("Tagged subjects for this video. E.g. Maths or Philosophy"),
                        fieldWithPath("badges").description("Tagged badges for this video. E.g. ad-free or Youtube"),

                        fieldWithPath("playback.type").description("Playback type, i.e. STREAM or YOUTUBE"),
                        fieldWithPath("playback.id").description("Id of this playback, useful for YOUTUBE type"),
                        fieldWithPath("playback.streamUrl").description("Streaming URL for this particular video"),
                        fieldWithPath("playback.thumbnailUrl").description("URL for a thumbnail of this video"),
                        fieldWithPath("playback.duration").description("Duration of this particular video in ISO-8601"),
                        fieldWithPath("playback._links.createPlaybackEvent.href").description("Link to post to for a createPlaybackEvent"),

                        fieldWithPath("legalRestrictions").description("Legal restrictions for this particular video if any"),

                        fieldWithPath("contentPartner").description("deprecated"), //TODO this should be renamed a "partner" makes no sense for API consumers
                        fieldWithPath("contentPartnerVideoId").description("deprecated"), //TODO This makes no sense in the API
                        fieldWithPath("type.id").description("deprecated"), // TODO We don't really care about this in the API
                        fieldWithPath("type.name").description("deprecated"), // TODO We should remodel this using the same filtering mechanisms values we provide
                        fieldWithPath("status").description("deprecated"), // TODO This should be available for internal use only
                        linksFieldDescriptor
                    )
                    , links(
                        linkWithRel("self").description("The video resource that was just retrieved")
                    )
                )
            )
            .`when`().get("/videos/{id}", "5c542abf5438cdbcb56df0bf").apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
    }

    @Test
    fun `video search`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-video-search"
                    , requestParameters(
                        parameterWithName("query").description("The text search query").attributes(Attributes.key("type").value("String")).attributes(Attributes.key("type").value("String")),
                        parameterWithName("size").optional().description("The amount of videos per page, 100 by default").attributes(Attributes.key("type").value("Number")),
                        parameterWithName("page").optional().description("Zero-index based page number, first page by default").attributes(Attributes.key("type").value("Number")),
                        parameterWithName("sort_by").optional().description("A key to sort the results by, currently only release_date is supported. Useful to get the latest videos for a search").attributes(Attributes.key("type").value("RELEASE_DATE")),
                        parameterWithName("min_duration").optional().description("Filters on the duration property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (PT6M5S)")),
                        parameterWithName("max_duration").optional().description("Filters on the duration property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (PT30S)")),
                        parameterWithName("released_date_from").optional().description("Filters on the releasedOn property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")),
                        parameterWithName("released_date_to").optional().description("Filters on the releasedOn property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")),
                        parameterWithName("source").optional().description("Filter by video source, e.g youtube or boclips").attributes(Attributes.key("type").value("youtube, boclips"))
                    )
                    , PayloadDocumentation.responseFields(
                        PayloadDocumentation.subsectionWithPath("_embedded.videos").description("Video resources array. See <<resources-video-access_response_fields,video>> for payload details"),
                        fieldWithPath("page.size").description("Amount of videos in the current page"),
                        fieldWithPath("page.totalElements").description("Total amount of videos for this search query across pages"),
                        fieldWithPath("page.totalPages").description("Total amount of pages for this search query"),
                        fieldWithPath("page.number").description("Number of the current page. Zero-index based")

                    )
                )
            )
            .`when`().get("/videos?query=test&page=0&size=1").apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
    }
}
