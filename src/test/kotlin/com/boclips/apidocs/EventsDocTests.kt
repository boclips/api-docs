package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import java.net.URI

class EventsDocTests : AbstractDocTests() {
    val videoId = "5c542abf5438cdbcb56df0bf"

    @Test
    fun `publishing playback event`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resources-events-publish-playback",
                    requestFields(
                        fieldWithPath("videoId").description("ID of the <<resources-videos,video>>"),
                        fieldWithPath("segmentStartSeconds").description("Second the video started its playback"),
                        fieldWithPath("segmentEndSeconds").description("Second the video ended its playback")
                    )
                )
            )
            .`when`().contentType(ContentType.JSON).body(
                """
                {
                  "videoId": "$videoId",
                  "segmentStartSeconds": 1,
                  "segmentEndSeconds": 3
                }
            """.trimIndent()
            )
            .post(createPlaybackEventLink())
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(HttpStatus.CREATED.value()))
    }

    @Test
    fun `publishing batch of playback events`() {
        given(stubOwnerSpec)
            .filter(
                document(
                    "resources-events-publish-batch-playback",
                    requestFields(
                        fieldWithPath("[*].videoId").description("ID of the <<resources-videos,video>>"),
                        fieldWithPath("[*].segmentStartSeconds").description("Second the video started its playback"),
                        fieldWithPath("[*].segmentEndSeconds").description("Second the video ended its playback"),
                        fieldWithPath("[*].captureTime").optional().description("Time when playback event was fired")
                    )
                )
            )
            .`when`().contentType(ContentType.JSON).body(
                """
                [{
                    "videoId": "$videoId",
                    "segmentStartSeconds": 1,
                    "segmentEndSeconds": 3,
                    "captureTime": "1997-07-16T19:20:30.45+01:00"
                },
                {
                    "videoId": "$videoId",
                    "segmentStartSeconds": 1,
                    "segmentEndSeconds": 3,
                    "captureTime": "1997-07-16T19:20:30.45+01:00"
                }]
            """.trimIndent()
            )
            .post(getPlaybackEventsLink())
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(HttpStatus.CREATED.value()))
    }

    fun createPlaybackEventLink(): URI {
        val video = videosClient.getVideo(videoId)
            return URI(video.playback?._links?.get("createPlaybackEvent")?.href!!)
    }

    fun getPlaybackEventsLink(): URI {
        return URI(links["createPlaybackEvents"] ?: error("Could not find link"))
    }
}
