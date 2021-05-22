package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.damnhandy.uri.template.UriTemplate
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class ChannelDocTests : AbstractDocTests() {
    @Test
    fun `getting single channel`() {
        val channel = channelsClient.getChannels()._embedded.channels[0]
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-channel-get",
                    pathParameters(
                        parameterWithName("id").description("The ID of the channel")
                    ),
                    responseFields(
                        fieldWithPath("id").description("The ID of the channel"),
                        fieldWithPath("name").description("The name of the channel"),
                        subsectionWithPath("legalRestriction").description("Text demonstrating the legal restrictions involved in using this channel's content"),
                        fieldWithPath("description").description("Text describing this channel's content"),
                        subsectionWithPath("contentCategories[*].key").ignored(),
                        subsectionWithPath("contentCategories[*].label").description("Content category label"),
                        subsectionWithPath("language.code").description("Language in 3 letter ISO-639-2 code format"),
                        subsectionWithPath("language.name").description("Name of the channel language"),
                        subsectionWithPath("_links").description("HAL links related to this collection"),
                        fieldWithPath("contentTypes").description("Channel types"),
                        fieldWithPath("notes").description("Custom notes about the channel"),
                        fieldWithPath("oneLineDescription").description("A snappy, high-energy description of the channel")
                    ),
                    links(
                        linkWithRel("self").description("Points to this channel")
                    )
                )
            )
            .`when`()
            .get(links["channel"], channel.id).apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `getting all channels`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-channels-get",
                    responseFields(
                        subsectionWithPath("_embedded.channels").description("Channels resources array. See the <<_retrieving_one_channel_response_fields,channel>> resource for payload details")
                    )
                )
            )
            .`when`()
            .get(UriTemplate.fromTemplate(links["channels"]).expand())
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
