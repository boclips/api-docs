package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.damnhandy.uri.template.UriTemplate
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class AlignmentsDocTests : AbstractDocTests() {
    private val stagingThemeId = "62eba02f51ecf2a9306c85ef"
    private val stagingThemeProvider = "openstax"

    @Test
    fun `Get all providers`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.providers[].name").description("Name of the curriculum or publisher"),
            fieldWithPath("_embedded.providers[].types")
                .description("The disciplines or school levels available by provider"),
            PayloadDocumentation.subsectionWithPath("_embedded.providers[]._links")
                .description("HAL links for the resource"),
            fieldWithPath("_embedded.providers[].description").ignored(),
            fieldWithPath("_embedded.providers[].logoUrl").ignored(),
            fieldWithPath("_embedded.providers[].defaultThemeLogoUrl").ignored(),
            fieldWithPath("_embedded.providers[].navigationPath").ignored()
        )
        RestAssured.given(stubOwnerSpec)
            .filter(
                document(
                    "resource-providers-get",
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get all providers",
                    false,
                    responseFields
                )
            )
            .`when`()
            .get(links["getAllProviders"]).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `Get a provider's themes`() {
        val pathParameters = RequestDocumentation.pathParameters(
            RequestDocumentation.parameterWithName("provider").description("ID of the provider")
        )
        val responseFields = responseFields(
            fieldWithPath("_embedded.themes[].id").description("ID of the theme"),
            fieldWithPath("_embedded.themes[].provider")
                .description("Name of the curriculum or publisher"),
            PayloadDocumentation.subsectionWithPath("_embedded.themes[]._links")
                .description("HAL links for the resource"),
            fieldWithPath("_embedded.themes[].type").description("The discipline or school level"),
            fieldWithPath("_embedded.themes[].title").description("The book or specific grade level"),
            fieldWithPath("_embedded.themes[].topics[].index").description("Recommended order of the topic"),
            fieldWithPath("_embedded.themes[].topics[].title").description("The chapter or cluster name"),
            fieldWithPath("_embedded.themes[].topics[].targets[].index").description("Recommended order of the target"),
            fieldWithPath("_embedded.themes[].topics[].targets[].title").description("The section or standard name"),
            fieldWithPath("_embedded.themes[].topics[].targets[].videoIds").description("Videos aligned to the target"),
            fieldWithPath("_embedded.themes[].logoUrl").ignored()
        )
        RestAssured.given(stubOwnerSpec)
            .filter(
                document(
                    "resource-provider-themes-get",
                    pathParameters,
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get a provider's themes",
                    false,
                    pathParameters,
                    responseFields
                )
            )
            .`when`()
            .get(links["getThemesByProvider"], "ngss").apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `get a provider theme by id`() {
        val responseFields = responseFields(
            fieldWithPath("id").description("ID of the theme"),
            fieldWithPath("provider")
                .description("Name of the curriculum or publisher"),
            PayloadDocumentation.subsectionWithPath("_links").description("HAL links for the resource"),
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
        val pathParameters = RequestDocumentation.pathParameters(
            RequestDocumentation.parameterWithName("provider").description("ID of the provider"),
            RequestDocumentation.parameterWithName("id").description("ID of the theme")
        )
        RestAssured.given(stubOwnerSpec)
            .filter(
                document(
                    "resource-provider-theme-get",
                    pathParameters,
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get a provider theme by id",
                    false,
                    pathParameters,
                    responseFields
                )
            )
            .`when`()
            .get(links["getThemesByProviderAndId"], stagingThemeProvider, stagingThemeId).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `Get themes by IDs`() {
        val responseFields = responseFields(
            fieldWithPath("_embedded.themes[].id").description("ID of the theme"),
            fieldWithPath("_embedded.themes[].provider")
                .description("Name of the curriculum or publisher"),
            PayloadDocumentation.subsectionWithPath("_embedded.themes[]._links")
                .description("HAL links for the resource"),
            fieldWithPath("_embedded.themes[].type").description("The discipline or school level"),
            fieldWithPath("_embedded.themes[].title").description("The book or specific grade level"),
            fieldWithPath("_embedded.themes[].topics[].index").description("Recommended order of the topic"),
            fieldWithPath("_embedded.themes[].topics[].title").description("The chapter or cluster name"),
            fieldWithPath("_embedded.themes[].topics[].targets[].index").description("Recommended order of the target"),
            fieldWithPath("_embedded.themes[].topics[].targets[].title").description("The section or standard name"),
            fieldWithPath("_embedded.themes[].topics[].targets[].videoIds").description("Videos aligned to the target"),
            fieldWithPath("_embedded.themes[].logoUrl").ignored()
        )
        val requestParameters = RequestDocumentation.requestParameters(
            RequestDocumentation.parameterWithName("id")
                .optional()
                .description("IDs of the Theme")
                .attributes(Attributes.key("type").value("List of Theme IDs"))
        )
        RestAssured.given(stubOwnerSpec)
            .filter(
                document(
                    "resource-themes-get",
                    requestParameters,
                    responseFields
                )
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get themes by IDs",
                    false,
                    requestParameters,
                    responseFields
                )
            )
            .`when`()
            .get(
                UriTemplate.fromTemplate(links["getThemesByIds"])
                    .set("id", setOf(stagingThemeId))
                    .expand()
            )
            .apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
