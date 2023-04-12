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

class AlignmentsDocTests : AbstractDocTests() {
    @Test
    fun `get all providers`() {
        RestAssured.given(stubOwnerSpec)
            .filter(
                RestAssuredRestDocumentation.document(
                    "resource-providers-get",
                    responseFields(
                        fieldWithPath("_embedded.providers[].name").description("Name of the provider"),
                        fieldWithPath("_embedded.providers[].types")
                            .description("Types available by provider"),
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
                            .description("Provider name"),
                        fieldWithPath("_embedded.themes[]._links")
                            .ignored(),
                        fieldWithPath("_embedded.themes[].type").description("Name of the type"),
                        fieldWithPath("_embedded.themes[].title").description("Name of the theme"),
                        fieldWithPath("_embedded.themes[].topics[].index").description("Recommended order of the topic"),
                        fieldWithPath("_embedded.themes[].topics[].title").description("Topic name"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].index").description("Recommended order of the target"),
                        fieldWithPath("_embedded.themes[].topics[].targets[].title").description("Target name"),
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
                            .description("Provider name"),
                        fieldWithPath("_links")
                            .ignored(),
                        fieldWithPath("type").description("Name of the type"),
                        fieldWithPath("title").description("Name of the theme"),
                        fieldWithPath("topics[].index").description("Recommended order of the topic"),
                        fieldWithPath("topics[].title").description("Topic name"),
                        fieldWithPath("topics[].targets[].index").description("Recommended order of the target"),
                        fieldWithPath("topics[].targets[].title").description("Target name"),
                        fieldWithPath("topics[].targets[].videoIds").description("Videos aligned to the target"),
                        PayloadDocumentation.subsectionWithPath("topics[].targets[].videos").description("Videos details"),
                        fieldWithPath("logoUrl").ignored()
                    )
                )
            )
            .`when`()
            .get(links["getThemesByProviderAndId"], theme.provider, theme.id).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
