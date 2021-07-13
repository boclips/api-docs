package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.UriTemplateHelper.stripOptionalParameters
import com.damnhandy.uri.template.UriTemplate
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class VideosDocTests : AbstractDocTests() {

    @Test
    fun `video details`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-video",
                    pathParameters(
                        parameterWithName("id").description("The ID of the video asset")
                    ),
                    responseFields(
                        beneathPath("playback").withSubsectionId("playback"),
                        fieldWithPath("type").description("Playback type, i.e. STREAM or YOUTUBE"),
                        fieldWithPath("id").description("Id of this playback, useful for YOUTUBE type"),
                        fieldWithPath("duration").description("Duration of this particular video in ISO-8601"),
                        fieldWithPath("_links.createPlaybackEvent.href").description("POST endpoint for a createPlaybackEvent. See more on events <<resources-events,here>>"),
                        fieldWithPath("_links.createPlaybackEvent.templated").ignored(),
                        fieldWithPath("_links.createPlayerInteractedWithEvent.href").description("POST endpoint for a createPlayerInteractedWithEvent"),
                        fieldWithPath("_links.createPlayerInteractedWithEvent.templated").ignored(),
                        fieldWithPath("_links.thumbnail.href").description("Thumbnail URL for the video. May be <<overview-interpolating-urls,templated>> with thumbnailWidth")
                            .optional(),
                        fieldWithPath("_links.thumbnail.templated").description("Tells whether the thumbnail link is <<overview-interpolating-urls,templated>>"),
                        fieldWithPath("_links.videoPreview.href").description("VideoPreview URL for the video. Templated with thumbnailWidth, and thumbnailCount")
                            .optional(),
                        fieldWithPath("_links.videoPreview.templated").ignored(),
                        fieldWithPath("_links.hlsStream.href").description("URL for the Apple HLS stream").optional(),
                        fieldWithPath("_links.hlsStream.templated").ignored()
                    ),
                    responseFields(
                        fieldWithPath("id").description("The unique identifier for this video, can be interpolated in templated links"),
                        fieldWithPath("title").description("Human readable title for this video"),
                        fieldWithPath("description").description("Description detailing what this video talks about"),
                        fieldWithPath("additionalDescription").description("Additional information to help improve the metadata"),
                        fieldWithPath("releasedOn").description("Date on which the video was originally released as stated by the content producer"),
                        fieldWithPath("updatedAt").description("Date when the most recent update occured"),
                        subsectionWithPath("subjects").description("Tagged Subject resources for this video. See <<resources-subjects,subject resource>> for payload details"),
                        fieldWithPath("badges").description("Tagged badges for this video. E.g. ad-free or Youtube"),
                        fieldWithPath("rating").description("Score of this video based on user rating. From 0 to 5"),
                        fieldWithPath("yourRating").description("Score you gave to this video. From 0 to 5"),
                        fieldWithPath("bestFor").description("Most appropriate use for this video"),
                        fieldWithPath("promoted").description("Promoted status of this video"),

                        subsectionWithPath("playback").description("Video Playback resource. See <<resources-video-access_response_fields-playback,playback>> for payload details"),

                        subsectionWithPath("attachments").description("List of resources attached to the video to help use the video in the classroom"),

                        fieldWithPath("legalRestrictions").description("Legal restrictions for this particular video if any"),
                        fieldWithPath("contentWarnings").description("Content warnings for this particular video if any"),

                        fieldWithPath("keywords").ignored(),

                        fieldWithPath("ageRange.label").optional().description("Age range in a human readable format"),
                        fieldWithPath("ageRange.min").optional().description("Minimum of age range for this video"),
                        fieldWithPath("ageRange.max").optional().description("Maximum of age range for this video"),
                        fieldWithPath("language.code").optional()
                            .description("The language of the video in the format of the ISO 639-2 standard"),
                        fieldWithPath("language.displayName").optional()
                            .description("The language of the video in a human readable format (e.g English)"),

                        fieldWithPath("createdBy").description("Who provided the video"),
                        linksFieldDescriptor
                    ),
                    links(
                        linkWithRel("self").description("The video resource that was just retrieved"),
                        linkWithRel("logInteraction").description("`POST` request to this URL will log user's interaction with this video"),
                        linkWithRel("rate").description("`PATCH` request to this URL will give this video a rating"),
                        linkWithRel("tag").description("`PATCH` request to this URL will tag this video"),
                        linkWithRel("transcript").description("`GET` to fetch transcripts of video")
                    )
                )
            )
            .`when`().get(stripOptionalParameters(links["video"]), "5c542abf5438cdbcb56df0bf").apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
    }

    @Test
    fun `video search`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-video-search"
                    , requestParameters(
                    parameterWithName("size").optional()
                        .description("The number of videos per page, 100 by default").attributes(
                            Attributes.key("type").value("Number")
                        ),
                    parameterWithName("page").optional()
                        .description("Zero-index based page number, first page by default").attributes(
                            Attributes.key("type").value("Number")
                        ),
                    parameterWithName("query").optional().description("The text search query").attributes(
                        Attributes.key(
                            "type"
                        ).value("String")
                    ),
                    parameterWithName("duration").optional()
                        .description("Filters on the video duration property. Provide duration ranges in the form `min[-max]`, ie `PT1M-PT6M`. These ranges are inclusive. This property supersedes the duration_min and duration_max properties.")
                        .attributes(
                            Attributes.key("type").value("Range of ISO-8601 (PT6M5S)")
                        ),
                    parameterWithName("duration_min").optional()
                        .description("Filters on the video duration property, this range is inclusive").attributes(
                            Attributes.key("type").value("ISO-8601 (PT6M5S)")
                        ),
                    parameterWithName("duration_max").optional()
                        .description("Filters on the video duration property, this range is inclusive").attributes(
                            Attributes.key("type").value("ISO-8601 (PT30S)")
                        ),
                    parameterWithName("released_date_from").optional()
                        .description("Filters on the video releasedOn property, this range is inclusive")
                        .attributes(
                            Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")
                        ),
                    parameterWithName("released_date_to").optional()
                        .description("Filters on the video releasedOn property, this range is inclusive")
                        .attributes(
                            Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")
                        ),
                    parameterWithName("updated_as_of").optional()
                        .description("Filters on the video updatedAt property, this range is inclusive")
                        .attributes(
                            Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)")
                        ),
                    parameterWithName("source").optional()
                        .description("Filter by video source, e.g youtube or boclips").attributes(
                            Attributes.key("type").value("YOUTUBE, BOCLIPS")
                        ),
                    parameterWithName("subject").optional()
                        .description("Filter by subject id - from the <<resources-subjects,list of subjects>>")
                        .attributes(
                            Attributes.key("type").value("Subject Id (e.g. '5cb499c9fd5beb428189454b')")
                        ),
                    parameterWithName("age_range_min").optional()
                        .description("Minimum age to filter from - it filters on the video age range property, and is inclusive. See <<resources-video-search-age-ranges,filter by age>> for more details.")
                        .attributes(
                            Attributes.key("type").value("Number")
                        ),
                    parameterWithName("age_range_max").optional()
                        .description("Maximum age to filter to - it filters on the video age range property, and is inclusive. See <<resources-video-search-age-ranges,filter by age>> for more details.")
                        .attributes(
                            Attributes.key("type").value("Number")
                        ),
                    parameterWithName("age_range").optional()
                        .description("Filter videos which cover at least 2 ages from a range in the video age range property. Provide age ranges in the form `lowerAge-upperAge`, ie `5-7`. See <<resources-video-search-age-ranges,filter by age>> for more details.")
                        .attributes(
                            Attributes.key("type").value("String")
                        ),
                    parameterWithName("duration_facets").optional()
                        .description("Override default facets for durations, see <<resources-video-search-facets,search facets>>.")
                        .attributes(
                            Attributes.key("type").value("Range of ISO-8601 (PT6M5S), e.g. PT0S-PT5M.")
                        ),
                    parameterWithName("age_range_facets").optional()
                        .description("Override default facets for age ranges, see <<resources-video-search-facets,search facets>>.")
                        .attributes(
                            Attributes.key("type").value("String, e.g. 3-5")
                        ),
                    parameterWithName("promoted").optional().description("Filter by promoted videos only")
                        .attributes(
                            Attributes.key("type").value("Boolean")
                        ),
                    parameterWithName("content_partner").optional()
                        .description("Deprecated in favour of channel. Filter by content partner, which is the provider of the video content. Use multiple times to search for multiple values, e.g. 'content_partner=first&content_partner=second'.")
                        .attributes(
                            Attributes.key("type").value("String (e.g. 'Bloomberg')")
                        ),
                    parameterWithName("channel").optional()
                        .description(
                                "Filter by channel IDs (channel is the provider of the video content). " +
                                        "Use multiple times to search for multiple values, " +
                                        "e.g. 'channel=5d5432448256f68bdcf75d53&channel=5d77b49698cfe500017e9856'. " +
                                        "Deprecated: filtering by channel names (it is still available, but " +
                                        "will be removed anytime soon)."
                        )
                        .attributes(
                            Attributes.key("type").value("String (e.g. '5d77b49698cfe500017e9856')")
                        ),
                    parameterWithName("type").optional()
                        .description("Filter responses by <<resources-video-types,video type>>").attributes(
                            Attributes.key("type").value("Enum")
                        ),
                    parameterWithName("best_for").optional()
                        .description("Filter responses by <<resources-tags,tag>> labels, exact matches when specifying multiple tags")
                        .attributes(
                            Attributes.key("type").value("List of strings (e.g 'explainer')")
                        ),
                    parameterWithName("sort_by").optional()
                        .description("A key to sort the results by, currently only release_date and rating are supported. This only sorts in a descending direction")
                        .attributes(
                            Attributes.key("type").value("RELEASE_DATE, RATING")
                        ),
                    parameterWithName("id").optional()
                        .description("Filter by video ids, this can be a comma separated list of video ids")
                        .attributes(
                            Attributes.key("type").value("Video ID (e.g '5cd9627d6c2905689d1c150c'")
                        )

                )
                    , responseFields(
                    subsectionWithPath("_embedded.videos").description("Video resources array. See <<resources-video-access_response_fields,video>> for payload details"),
                    subsectionWithPath("_embedded.facets").description("Search facets for age ranges, durations and subjects"),
                    *pageSpecificationResponseFields
                )
                )
            )
            .`when`()
            .get(
                UriTemplate.fromTemplate(links["searchVideos"])
                    .set("query", "genetic")
                    .set("page", 0)
                    .set("size", 1)
                    .set("duration_min", "PT1M")
                    .set("duration_max", "PT3M")
                    .set("released_date_from", "2018-01-01")
                    .set("released_date_to", "2019-06-01")
                    .set("source", "boclips")
                    .set("sort_by", "RELEASE_DATE")
                    .set("channel", "5cf140c4c1475c47f7178679")
                    .set("type", "NEWS")
                    .expand()
            )
            .apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
            .and().body("_embedded.videos[0].title", Matchers.not(Matchers.isEmptyOrNullString()))
            .and().body("_embedded.facets.ageRanges", Matchers.not(Matchers.isEmptyOrNullString()))
            .and().body("_embedded.facets.durations", Matchers.not(Matchers.isEmptyOrNullString()))
            .and().body("_embedded.facets.subjects", Matchers.not(Matchers.isEmptyOrNullString()))
    }
}
