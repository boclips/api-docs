package com.boclips.apidocs

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
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
                                fieldWithPath("rating").description("Score of this video based on user rating. From 0 to 5."),

                                fieldWithPath("playback.type").description("Playback type, i.e. STREAM or YOUTUBE"),
                                fieldWithPath("playback.id").description("Id of this playback, useful for YOUTUBE type"),
                                fieldWithPath("playback.streamUrl").description("Streaming URL for this particular video"),
                                fieldWithPath("playback.thumbnailUrl").description("URL for a thumbnail of this video"),
                                fieldWithPath("playback.duration").description("Duration of this particular video in ISO-8601"),
                                fieldWithPath("playback._links.createPlaybackEvent.href").description("POST endpoint for a createPlaybackEvent"),
                                fieldWithPath("playback._links.createPlayerInteractedWithEvent.href").description("POST endpoint for a createPlayerInteractedWithEvent"),

                                fieldWithPath("legalRestrictions").description("Legal restrictions for this particular video if any"),

                                fieldWithPath("ageRange.label").optional().description("Age range in a human readable format"),
                                fieldWithPath("ageRange.min").optional().description("Minimum of age range for this video"),
                                fieldWithPath("ageRange.max").optional().description("Maximum of age range for this video"),

                                fieldWithPath("contentPartner").description("deprecated"), //TODO this should be renamed a "partner" makes no sense for API consumers
                                fieldWithPath("contentPartnerVideoId").description("deprecated"), //TODO This makes no sense in the API
                                fieldWithPath("type.id").description("deprecated"), // TODO We don't really care about this in the API
                                fieldWithPath("type.name").description("deprecated"), // TODO We should remodel this using the same filtering mechanisms values we provide
                                fieldWithPath("status").description("deprecated"), // TODO This should be available for internal use only
                                fieldWithPath("hiddenFromSearchForDeliveryMethods").description("deprecated"), // TODO This hsould be available for internal use only
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
                                //Pagination
                                parameterWithName("size").optional().description("The number of videos per page, 100 by default").attributes(Attributes.key("type").value("Number")),
                                parameterWithName("page").optional().description("Zero-index based page number, first page by default").attributes(Attributes.key("type").value("Number")),
                                //Filters
                                parameterWithName("query").optional().description("The text search query").attributes(Attributes.key("type").value("String")),
                                parameterWithName("duration_min").optional().description("Filters on the video duration property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (PT6M5S)")),
                                parameterWithName("duration_max").optional().description("Filters on the video duration property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (PT30S)")),
                                parameterWithName("released_date_from").optional().description("Filters on the video releasedOn property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")),
                                parameterWithName("released_date_to").optional().description("Filters on the video releasedOn property, this range is inclusive").attributes(Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")),
                                parameterWithName("source").optional().description("Filter by video source, e.g youtube or boclips").attributes(Attributes.key("type").value("youtube, boclips")),
                                parameterWithName("subject").optional().description("Filter by subject id - from the <<resources-subjects,list of subjects>>").attributes(Attributes.key("type").value("Subject Id (5cb499c9fd5beb428189454b)")),
                                parameterWithName("age_range_min").optional().description("Minimum age to filter from - it filters on the video age range property, and is inclusive.").attributes(Attributes.key("type").value("Number")),
                                parameterWithName("age_range_max").optional().description("Maximum age to filter to - it filters on the video age range property, and is inclusive.").attributes(Attributes.key("type").value("Number")),
                                //Sorting
                                parameterWithName("sort_by").optional().description("A key to sort the results by, currently only release_date is supported. Useful to search for the latest videos").attributes(Attributes.key("type").value("RELEASE_DATE"))

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
                .`when`().get("/videos" +
                        "?query=genetic" +
                        "&page=0" +
                        "&size=1" +
                        "&duration_min=PT1M" +
                        "&duration_max=PT3M" +
                        "&released_date_from=2018-01-01" +
                        "&released_date_to=2019-06-01" +
                        "&source=boclips" +
                        "&sort_by=RELEASE_DATE").apply { println(prettyPrint()) }
                .then().assertThat().statusCode(`is`(200))
                .and().body("_embedded.videos[0].title", Matchers.not(Matchers.isEmptyString()))
    }
}
