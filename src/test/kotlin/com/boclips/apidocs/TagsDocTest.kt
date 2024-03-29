package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

class TagsDocTest : AbstractDocTests() {
    @Test
    fun `Tags test`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.tags[].id").description("ID of the tag"),
            fieldWithPath("_embedded.tags[].label").description("Human readable tag label, this can be used for filtering videos"),
            fieldWithPath("_embedded.tags[].userId").description("The ID of the user that tagged the video with the given tag"),
            subsectionWithPath("_embedded.tags[]._links").description("HAL links for the tag resource"),
            subsectionWithPath("_links").description("HAL links for the tag collection resource")
        )
        val responseLinks = links(
            linkWithRel("self").description("The tag collection resource that was just retrieved")
        )
        given(stubOwnerSpec)
            .filter(
                document(
                    "resource-tags",
                    responseFields,
                    responseLinks
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get tags",
                    false,
                    responseFields,
                    responseLinks,
                )
            )
            .`when`()
            .get(links["tags"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
