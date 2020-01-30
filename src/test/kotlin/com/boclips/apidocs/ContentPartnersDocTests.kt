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

class ContentPartnersDocTests : AbstractDocTests() {
    @Test
    fun `getting single content partner`() {
        val contentPartner = videoServiceClient.contentPartners[0]
        given(documentationSpec)
            .filter(
                document(
                    "resource-content-partner-get",
                    pathParameters(
                        parameterWithName("id").description("The ID of the content partner")
                    ),
                    responseFields(
                        fieldWithPath("id").description("The ID of the content partner"),
                        fieldWithPath("name").description("The name of the content partner"),
                        subsectionWithPath("ageRange").description("The age ranges served by the content partner"),
                        subsectionWithPath("legalRestriction").description("Text demonstrating the legal restrictions involved in using this partner's content"),
                        subsectionWithPath("description").description("Text describing this partner's content"),
                        subsectionWithPath("contentCategories[*].key").ignored(),
                        subsectionWithPath("contentCategories[*].label").description("Content category label"),
                        subsectionWithPath("language.code").description("Language in 3 letter ISO-639-2 code format"),
                        subsectionWithPath("language.name").description("Name of the content partner language"),
                        subsectionWithPath("_links").description("HAL links related to this collection"),
                        subsectionWithPath("hubspotId").ignored(),
                        subsectionWithPath("awards").description("Content partner awards"),
                        subsectionWithPath("notes").description("Custom notes about the content partner"),

                        fieldWithPath("official").ignored(),
                        fieldWithPath("distributionMethods").ignored(),
                        fieldWithPath("currency").ignored()
                    ),
                    links(
                        linkWithRel("self").description("Points to this contentPartner")
                    )
                )
            )
            .`when`()
            .get(links["contentPartner"], contentPartner.contentPartnerId.value).apply { println(prettyPrint()) }
            .then()
            .assertThat().statusCode(`is`(200))
    }

    @Test
    fun `getting all content partners`() {
        given(documentationSpec)
            .filter(
                document(
                    "resource-content-partners-get",
                    responseFields(
                        subsectionWithPath("_embedded.contentPartners").description("Content partners resources array. See the <<_retrieving_one_content_partner_response_fields,content partner>> resource for payload details")
                    )
                )
            )
            .`when`()
            .get(UriTemplate.fromTemplate(links["contentPartners"]).expand())
            .then()
            .assertThat().statusCode(`is`(200))
    }
}
