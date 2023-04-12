package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.damnhandy.uri.template.UriTemplate
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.restdocs.snippet.Attributes

class AlignmentsDocTests : AbstractDocTests() {
    @Test
    fun `get all providers`() {
        RestAssured.given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentation.document(
                    "resource-providers-get",
                    responseFields(
                        fieldWithPath("_embedded.providers[].name").description("Name of the curriculum or publisher"),
                        fieldWithPath("_embedded.providers[].types")
                            .description("The disciplines or school levels available by provider"),
                        fieldWithPath("_embedded.providers[].description").ignored(),
                        fieldWithPath("_embedded.providers[].logoUrl").ignored(),
                        fieldWithPath("_embedded.providers[].defaultThemeLogoUrl").ignored(),
                        fieldWithPath("_embedded.providers[].navigationPath").ignored()
                    )
                )
            )
            .`when`()
            .get(links["getAllProviders"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `get a provider themes`() {
        RestAssured.given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentation.document(
                    "resource-provider-themes-get",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("provider").description("ID of the provider")
                    ),
                    responseFields(
                        fieldWithPath("_embedded.themes[].id").description("ID of the theme"),
                        fieldWithPath("_embedded.themes[].provider")
                            .description("Name of the curriculum or publisher"),
                        fieldWithPath("_embedded.themes[]._links")
                            .ignored(),
                        fieldWithPath("_embedded.themes[].type").description("The discipline or school level"),
                        fieldWithPath("_embedded.themes[].title").description("The book or specific grade level"),
                        fieldWithPath("_embedded.themes[].topics[].index").description("Recommended order of the topic"),
                        fieldWithPath("_embedded.themes[].topics[].title").description("The chapter or cluster name"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].index").description("Recommended order of the target"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].title").description("The section or standard name"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].videoIds").description("Videos aligned to the target"),
                        fieldWithPath("_embedded.themes[].logoUrl").ignored()
                    )
                )
            )
            .`when`()
            .get(links["getThemesByProvider"], "ngss").apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `get a provider theme by id`() {
        val theme = alignmentClient.getThemes()._embedded.themes[0]
        RestAssured.given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentation.document(
                    "resource-provider-theme-get",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("provider").description("ID of the provider"),
                        RequestDocumentation.parameterWithName("id").description("ID of the theme")
                    ),
                    responseFields(
                        fieldWithPath("id").description("ID of the theme"),
                        fieldWithPath("provider")
                            .description("Name of the curriculum or publisher"),
                        fieldWithPath("_links")
                            .ignored(),
                        fieldWithPath("type").description("The discipline or school level"),
                        fieldWithPath("title").description("The book or specific grade level"),
                        fieldWithPath("topics[].index").description("Recommended order of the topic"),
                        fieldWithPath("topics[].title").description("The chapter or cluster name"),
                        fieldWithPath("topics[].targets[].index").description("Recommended order of the target"),
                        fieldWithPath("topics[].targets[].title").description("The section or standard name"),
                        fieldWithPath("topics[].targets[].videoIds").description("Videos aligned to the target"),
                        PayloadDocumentation.subsectionWithPath("topics[].targets[].videos")
                            .description("Videos details"),
                        fieldWithPath("logoUrl").ignored()
                    )
                )
            )
            .`when`()
            .get(links["getThemesByProviderAndId"], theme.provider, theme.id).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `get themes by ids`() {
        val theme = alignmentClient.getThemes()._embedded.themes[0]
        RestAssured.given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentation.document(
                    "resource-themes-get",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("id")
                            .optional()
                            .description("IDs of the Theme")
                            .attributes(Attributes.key("type").value("List of Theme IDs"))
                    ),
                    responseFields(
                        fieldWithPath("_embedded.themes[].id").description("ID of the theme"),
                        fieldWithPath("_embedded.themes[].provider")
                            .description("Name of the curriculum or publisher"),
                        fieldWithPath("_embedded.themes[]._links")
                            .ignored(),
                        fieldWithPath("_embedded.themes[].type").description("The discipline or school level"),
                        fieldWithPath("_embedded.themes[].title").description("The book or specific grade level"),
                        fieldWithPath("_embedded.themes[].topics[].index").description("Recommended order of the topic"),
                        fieldWithPath("_embedded.themes[].topics[].title").description("The chapter or cluster name"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].index").description("Recommended order of the target"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].title").description("The section or standard name"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].videoIds").description("Videos aligned to the target"),
                        fieldWithPath("_embedded.themes[].logoUrl").ignored()
                    )
                )
            )
            .`when`()
            .get(
                UriTemplate.fromTemplate(links["getThemesByIds"])
                    .set("id", setOf(theme.id))
                    .expand()
            )
            .apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
