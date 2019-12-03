package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.videos.service.client.VideoId
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import java.net.URI
import java.util.UUID

class EventsDocTests : AbstractDocTests() {
    @Test
    fun `publishing playback event`() {
        given(documentationSpec)
            .filter(
                document(
                    "resources-events-publish-playback",
                    requestFields(
                        fieldWithPath("playerId").description("Unique ID of the video player"),
                        fieldWithPath("videoId").description("ID of the <<resources-videos,video>>"),
                        fieldWithPath("segmentStartSeconds").description("Second the video started its playback"),
                        fieldWithPath("segmentEndSeconds").description("Second the video ended its playback")
                    )
                )
            )
            .`when`().contentType(ContentType.JSON).body(
                """
                {
                  "playerId": "${UUID.randomUUID()}",
                  "videoId": "$videoId",
                  "segmentStartSeconds": 1,
                  "segmentEndSeconds": 3
                }
            """.trimIndent()
            ).post(createPlaybackEventLink)
            .apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(HttpStatus.CREATED.value()))
    }

    val videoId = "5c542abf5438cdbcb56df0bf"

    lateinit var createPlaybackEventLink: String

    @BeforeEach
    fun setupTestData() {
        val video = videoServiceClient.get(
            VideoId(URI("https://api.staging-boclips.com/v1/videos/$videoId"))
        )
        createPlaybackEventLink = video.playback.links.createPlaybackEvent.href
    }
}
