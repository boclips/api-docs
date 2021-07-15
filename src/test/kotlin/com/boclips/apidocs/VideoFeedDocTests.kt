package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.damnhandy.uri.template.UriTemplate
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class VideoFeedDocTests : AbstractDocTests() {

    @Test
    fun `video feed details`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-video-feed",
                    requestParameters(
                        parameterWithName("size")
                            .optional()
                            .description("The number of videos per page, 1000 by default and is also the max size")
                            .attributes(Attributes.key("type").value("Number")),
                        parameterWithName("cursor_id")
                            .optional()
                            .description("This is set explicitly in the next link and you should never have to set it")
                            .attributes(Attributes.key("type").value("String")),
                        parameterWithName("updated_as_of")
                            .optional()
                            .description("Filters on the video updatedAt property, this range is inclusive")
                            .attributes(Attributes.key("type").value("ISO-8601 (YYYY-MM-DD)"))
                    ),
                    responseFields(
                        subsectionWithPath("_embedded.videos")
                            .description("Video resources array. See <<resources-video-access_response_fields,video>> for payload details"),
                        subsectionWithPath("_links").description("HAL links related to this collection")
                    ),
                    links(
                        linkWithRel("next")
                            .description("The link to the next page of videos. This will only be valid for 5 minutes, also this will not be present once you have got to the end of results.")
                    )
                )
            )
            .`when`()
            .get(
                UriTemplate.fromTemplate(links["videoFeed"])
                    .set("size", "10")
                    .expand()
            )
            .apply { println(prettyPrint()) }
            .then().assertThat().statusCode(`is`(200))
            .and().body("_embedded.videos[0].title", Matchers.not(Matchers.isEmptyOrNullString()))
    }
}
