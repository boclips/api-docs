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
                        fieldWithPath("segmentEndSeconds").description("Second the video ended its playback"),
                        fieldWithPath("userId").optional().description("ID of the user who initiated the playback")
                    )
                )
            )
            .`when`().contentType(ContentType.JSON).body(
                """
                {
                  "videoId": "$videoId",
                  "segmentStartSeconds": 1,
                  "segmentEndSeconds": 3,
                  "userId": "f0e8d794-1d7e-4944-9705-e16946c7b694"
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
                        fieldWithPath("[*].captureTime").type("ISO-8601 (YYYY-MM-DDThh:mm:ss.sTZD)").description("Time when playback event was fired"),
                        fieldWithPath("[*].userId").optional().description("ID of the user who initiated the playback")
                    )
                )
            )
            .`when`().contentType(ContentType.JSON).body(
                """
                [{
                    "videoId": "$videoId",
                    "segmentStartSeconds": 1,
                    "segmentEndSeconds": 3,
                    "captureTime": "1997-07-16T19:20:30.45+01:00",
                    "userId": "05c4b56f-1ca2-42ac-b992-2b254584ea29"
                },
                {
                    "videoId": "$videoId",
                    "segmentStartSeconds": 1,
                    "segmentEndSeconds": 3,
                    "captureTime": "1997-07-16T19:20:30.45+01:00",
                    "userId": "b51a69eb-6977-4766-9f76-7b1b7dc0b953"
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
