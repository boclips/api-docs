package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.UriTemplateHelper.stripOptionalParameters
import com.damnhandy.uri.template.UriTemplate
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class VideosDocTests : AbstractDocTests() {

    @Test
    fun `Get Video By ID`() {
        val videoResponseFields = responseFields(
            fieldWithPath("id").description("The unique identifier for this video, can be interpolated in templated links"),
            fieldWithPath("title").description("Human readable title for this video"),
            fieldWithPath("description").description("Description detailing what this video talks about"),
            fieldWithPath("additionalDescription").description("Deprecated we are planning to drop support for this field. Additional information to help improve the metadata"),
            fieldWithPath("releasedOn").description("Date on which the video was originally released as stated by the content producer"),
            fieldWithPath("updatedAt").description("Date when the most recent update occured"),
            subsectionWithPath("subjects").description("Tagged Subject resources for this video. See <<resources-subjects,subject resource>> for payload details"),
            fieldWithPath("badges").description("Tagged badges for this video. E.g. ad-free"),
            subsectionWithPath("bestFor").description("List of best for labels. See <<resources-video-access_response_fields-bestFor,bestFor>> for payload details"),
            fieldWithPath("promoted").description("Promoted status of this video"),
            fieldWithPath("type").description("Content type of this video"),

            subsectionWithPath("playback").description("Video Playback resource. See <<resources-video-access_response_fields-playback,playback>> for payload details"),
            fieldWithPath("playback.type").description("Playback type, i.e. STREAM "),
            fieldWithPath("playback.id").description("Id of this playback"),
            fieldWithPath("playback.duration").description("Duration of this particular video in ISO-8601"),
            fieldWithPath("playback._links.createPlaybackEvent.href").description("POST endpoint for a createPlaybackEvent. See more on events <<resources-events,here>>"),
            fieldWithPath("playback._links.createPlaybackEvent.templated").ignored(),
            fieldWithPath("playback._links.createPlayerInteractedWithEvent.href").description("POST endpoint for a createPlayerInteractedWithEvent"),
            fieldWithPath("playback._links.createPlayerInteractedWithEvent.templated").ignored(),
            fieldWithPath("playback._links.thumbnail.href").description("Thumbnail URL for the video. May be <<overview-interpolating-urls,templated>> with thumbnailWidth")
                .optional(),
            fieldWithPath("playback._links.thumbnail.templated").description("Tells whether the thumbnail link is <<overview-interpolating-urls,templated>>"),
            fieldWithPath("playback._links.videoPreview.href").description("VideoPreview URL for the video. Templated with thumbnailWidth, and thumbnailCount")
                .optional(),
            fieldWithPath("playback._links.videoPreview.templated").ignored(),
            fieldWithPath("playback._links.hlsStream.href").description("URL for the Apple HLS stream. Please note this has a lifespan of 48 hours, beyond this the video will need to be retrieved again")
                .optional(),
            fieldWithPath("playback._links.hlsStream.templated").ignored(),

            subsectionWithPath("attachments").description("List of resources attached to the video to help use the video in the classroom"),

            fieldWithPath("legalRestrictions").description("Legal restrictions for this particular video if any"),
            fieldWithPath("bestFor.label").description("A <<resources-tags,tag>> label, to describe the video's learning objective")
                .type("String").optional(),
            subsectionWithPath("contentWarnings").description("Content warnings for this particular video if any. See <<resources-video-access_response_fields-contentWarnings,contentWarnings>> for payload details"),

            fieldWithPath("keywords").ignored(),
            fieldWithPath("contentWarnings.id").description("ID of the content warning").type("String").optional(),
            fieldWithPath("contentWarnings.label").description("Label describing the content warning").type("String")
                .optional(),
            fieldWithPath("contentWarnings._links.self.rel").type("String").ignored().optional(),
            fieldWithPath("contentWarnings._links.self.href").type("String").ignored().optional(),

            subsectionWithPath("educationLevels").description("Education levels this video is suitable for. See <<resources-education-levels,education levels resource>> for more details"),
            fieldWithPath("ageRange.label").optional()
                .description("Deprecated in favour of educationLevels. Age range in a human readable format"),
            fieldWithPath("ageRange.min").optional()
                .description("Deprecated in favour of educationLevels. Minimum of age range for this video"),
            fieldWithPath("ageRange.max").optional()
                .description("Deprecated in favour of educationLevels. Maximum of age range for this video"),
            fieldWithPath("language.code").optional()
                .description("The language of the video in the format of the ISO 639-2 standard"),
            fieldWithPath("language.displayName").optional()
                .description("The language of the video in a human readable format (e.g English)"),
            fieldWithPath("cefrLevel").optional()
                .description("The CEFR level of the video (e.g C1)"),
            fieldWithPath("contentCategories").optional()
                .description("Tagged subtypes for this video (e.g Animation)"),
            fieldWithPath("maxLicenseDurationYears").ignored(),
            fieldWithPath("restrictions.editing.permission").ignored(),
            fieldWithPath("restrictions.editing.editingInfo").ignored(),
            fieldWithPath("restrictions.territory.type").ignored(),
            fieldWithPath("restrictions.territory.territories").ignored(),
            fieldWithPath("restrictions.territory.additionalTerritoryInfo").ignored(),
            fieldWithPath("restrictions.video").ignored(),

            fieldWithPath("createdBy").description("Who provided the video"),
            fieldWithPath("availability.availableUntil")
                .optional()
                .description("If provided, the video will be only available until this date")
                .type("ISO-8601 (YYYY-MM-DD)"),
            linksFieldDescriptor
        )
        val videoResponseLinks = links(
            linkWithRel("self").description("The video resource that was just retrieved"),
            linkWithRel("logInteraction").description("`POST` request to this URL will log user's interaction with this video"),
            linkWithRel("tag").optional().description("`PATCH` request to this URL will tag this video"),
            linkWithRel("transcript").description("`GET` to fetch transcripts of video")
        )

        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-video",
                    pathParameters(
                        parameterWithName("id").description("The ID of the video asset")
                    ),
                    videoResponseFields,
                    videoResponseLinks
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}", "Get video endpoint", false,
                    pathParameters(
                        parameterWithName("id").description("The ID of the video asset")
                    ),
                    videoResponseFields,
                    videoResponseLinks,
                )
            )
            .`when`().get(stripOptionalParameters(links["video"]), "5c542abf5438cdbcb56df0bf")
            .apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
    }

    @Test
    fun `Search For Videos`() {
        val requestParameters = requestParameters(
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
                .description("Filter by video source, e.g boclips").attributes(
                    Attributes.key("type").value("BOCLIPS")
                ),
            parameterWithName("subject").optional()
                .description("Filter by subject id - from the <<resources-subjects,list of subjects>>")
                .attributes(
                    Attributes.key("type").value("Subject Id (e.g. '5cb499c9fd5beb428189454b')")
                ),
            parameterWithName("education_level").optional()
                .description("Filter by education level. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<resources-education-levels,education levels resource>>")
                .attributes(
                    Attributes.key("type").value("String")
                ),
            parameterWithName("age_range_min").optional()
                .description("Deprecated in favour of education_level. Minimum age to filter from - it filters on the video age range property, and is inclusive.")
                .attributes(
                    Attributes.key("type").value("Number")
                ),
            parameterWithName("age_range_max").optional()
                .description("Deprecated in favour of education_level. Maximum age to filter to - it filters on the video age range property, and is inclusive")
                .attributes(
                    Attributes.key("type").value("Number")
                ),
            parameterWithName("age_range").optional()
                .description("Deprecated in favour of education_level. Filter videos which cover at least 2 ages from a range in the video age range property.")
                .attributes(
                    Attributes.key("type").value("String")
                ),
            parameterWithName("cefr_level").optional()
                .description("Filter by CEFR level. Multiple values can be specified (comma separated, or by repeating the parameter). Possible values are: [A1, A2, B1, B2, C1, C2]")
                .attributes(
                    Attributes.key("type").value("String")
                ),
            parameterWithName("duration_facets").optional()
                .description("Override default facets for durations, see <<resources-video-search-facets,search facets>>.")
                .attributes(
                    Attributes.key("type").value("Range of ISO-8601 (PT6M5S), e.g. PT0S-PT5M.")
                ),
            parameterWithName("include_education_level_facets").optional()
                .description("Indicates whether to include education level facets into search results.")
                .attributes(
                    Attributes.key("type").value("Boolean")
                ),
            parameterWithName("age_range_facets").optional()
                .description("Deprecated. Override default facets for age ranges, see <<resources-video-search-facets,search facets>>.")
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
            parameterWithName("include_channel_facets").optional()
                .description("Indicates whether to include channel facets into search results.")
                .attributes(
                    Attributes.key("type").value("Boolean")
                ),
            parameterWithName("type").optional()
                .description("Filter responses by <<resources-video-types,video type>>").attributes(
                    Attributes.key("type").value("Enum")
                ),
            parameterWithName("best_for").optional()
                .description("Filter responses by <<resources-tags,tag>> labels")
                .attributes(
                    Attributes.key("type").value("List of strings (e.g 'explainer')")
                ),
            parameterWithName("sort_by").optional()
                .description("A key to sort the results by, currently only release_date is supported. This only sorts in a descending direction")
                .attributes(
                    Attributes.key("type").value("RELEASE_DATE")
                ),
            parameterWithName("id").optional()
                .description("Filter by video ids, this can be a comma separated list of video ids")
                .attributes(
                    Attributes.key("type").value("Video ID (e.g '5cd9627d6c2905689d1c150c'")
                ),
            parameterWithName("language").optional()
                .description(
                    "Filter by language codes (ISO 639-2 language code). " +
                        "Use multiple times to search for multiple values, " +
                        "e.g. 'language=eng&language=spa'."
                )
                .attributes(Attributes.key("type").value("String (e.g 'eng')")),
            parameterWithName("ngss_code").optional()
                .description("Filter by NGSS code. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<_retrieving_all_supported_ngss_codes,retrieving all NGSS codes>>")
                .attributes(
                    Attributes.key("type").value("String (eg. 'LS4')")
                ),
            parameterWithName("ngss_grade").optional()
                .description("Filter by NGSS grade. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<_retrieving_all_supported_ngss_grades,retrieving all NGSS grades>>")
                .attributes(
                    Attributes.key("type").value("String (eg. 'K-2')")
                ),
            parameterWithName("subtype").optional()
                .description("Filter by video subtype. Multiple values can be specified (comma separated, or by repeating the parameter). Possible values are: ['ANIMATION', 'CHALK_AND_TALK_PRESENTATION', 'DEMONSTRATION', 'DOCUMENTARY', 'HISTORICAL_ARCHIVE', 'INTERVIEW', 'LIVE_ARTS_PERFORMANCES', 'TALKING_HEAD']")
                .attributes(
                    Attributes.key("type").value("String (eg. 'ANIMATION')")
                )
        )
        val responseFields = responseFields(
            subsectionWithPath("_embedded.videos").description("Video resources array. See <<resources-video-access_response_fields,video>> for payload details"),
            subsectionWithPath("_embedded.facets").description("Search facets for durations, subjects and education levels"),
            *pageSpecificationResponseFields
        )
        given(stubOwnerSpec)
            .filter(document("resource-video-search", requestParameters, responseFields))
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Video Search Endpoint",
                    false,
                    requestParameters,
                    responseFields
                )
            )
            .`when`()
            .get(
                UriTemplate.fromTemplate(links["searchVideos"])
                    .set("query", "medical")
                    .set("page", 0)
                    .set("size", 1)
                    .set("duration_min", "PT1M")
                    .set("duration_max", "PT30M")
                    .set("released_date_from", "2006-01-01")
                    .set("released_date_to", "2007-06-01")
                    .set("source", "boclips")
                    .set("sort_by", "RELEASE_DATE")
                    .set("channel", "5cf140c4c1475c47f7178678")
                    .set("type", "INSTRUCTIONAL")
                    .expand()
            )
            .apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
            .and().body("_embedded.videos[0].title", Matchers.not(Matchers.emptyOrNullString()))
            .and().body("_embedded.facets.ageRanges", Matchers.not(Matchers.emptyOrNullString()))
            .and().body("_embedded.facets.durations", Matchers.not(Matchers.emptyOrNullString()))
            .and().body("_embedded.facets.subjects", Matchers.not(Matchers.emptyOrNullString()))
    }
}
