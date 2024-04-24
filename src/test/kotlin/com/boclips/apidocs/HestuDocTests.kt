package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class HestuDocTests : AbstractDocTests() {
    private val productionVideoId = "5c54d67ed8eafeecae2041c2"
    private val LEARNING_OUTCOMES_PATH = "/v1/videos/{video_id}/learning-outcomes"
    private val ASSESSMENT_QUESTIONS_PATH = "/v1/videos/{video_id}/assessment-questions"
    private val HIGHLIGHT_PATH = "/v1/highlights/{highlightId}"
    private val HIGHLIGHTS_PATH = "/v1/highlights"
    lateinit var hestuDocumentationSpec: RequestSpecification

    @BeforeEach
    fun setupDocumentationSpec(restDocumentation: RestDocumentationContextProvider) {
        hestuDocumentationSpec = RequestSpecificationFactory.createFor(
            accessToken = productionUserAccessToken,
            restDocumentation = restDocumentation,
            baseUri = "https://api.boclips.com",
        )
    }

    @Test
    fun `Get learning outcomes`(restDocumentation: RestDocumentationContextProvider) {
        val responseFields = responseFields(
            fieldWithPath("learningOutcomes").description("Learning outcomes of a given video"),
        )

        val pathParameters = RequestDocumentation.pathParameters(
            RequestDocumentation.parameterWithName("video_id").description("ID of the video"),
        )

        RestAssured.given(hestuDocumentationSpec)
            .filter(
                document(
                    "learning-outcomes-get",
                    pathParameters,
                    responseFields,
                ),
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get learning outcomes of a video",
                    false,
                    pathParameters,
                    responseFields,
                ),
            )
            .`when`()
            .get(LEARNING_OUTCOMES_PATH, productionVideoId).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `Get assessment questions`(restDocumentation: RestDocumentationContextProvider) {
        val responseFields = responseFields(
            fieldWithPath("assessmentQuestions").description("Assessment questions of a given video"),
        )

        val pathParameters = RequestDocumentation.pathParameters(
            RequestDocumentation.parameterWithName("video_id").description("ID of the video"),
        )

        RestAssured.given(hestuDocumentationSpec)
            .filter(
                document(
                    "assessment-questions-get",
                    pathParameters,
                    responseFields,
                ),
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get assessment questions of a video",
                    false,
                    responseFields,
                ),
            )
            .`when`()
            .get(ASSESSMENT_QUESTIONS_PATH, productionVideoId).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    @Disabled
    fun `Get highlight`(restDocumentation: RestDocumentationContextProvider) {
        val responseFields = responseFields(
            fieldWithPath("getHighlight").description("Get a given highlight by ID"),
        )

        val pathParameters = RequestDocumentation.pathParameters(
            RequestDocumentation.parameterWithName("highlightId").description("ID of the highlight"),
        )

        RestAssured.given(hestuDocumentationSpec)
            .filter(
                document(
                    "highlight-get",
                    pathParameters,
                    responseFields,
                ),
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get a given highlight by ID",
                    false,
                    responseFields,
                ),
            )
            .`when`()
            .get(HIGHLIGHT_PATH, productionVideoId).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    @Disabled
    fun `Get highlights`(restDocumentation: RestDocumentationContextProvider) {
        val responseFields = responseFields(
            fieldWithPath("getHighlights").description("Get filtered highlights"),
        )

        // TODO("Review the types, descriptions, and optionality of these params w/ DS)
        val requestParameters = RequestDocumentation.requestParameters(
            RequestDocumentation.parameterWithName("threshold")
                .optional()
                .description("")
                .attributes(Attributes.key("type").value("Number")),
            RequestDocumentation.parameterWithName("size")
                .optional()
                .description("")
                .attributes(Attributes.key("type").value("Number")),

            RequestDocumentation.parameterWithName("query")
                .optional()
                .description("")
                .attributes(Attributes.key("type").value("String")),

            RequestDocumentation.parameterWithName("education_level")
                .optional()
                .description("")
                .attributes(Attributes.key("type").value("String")),

            RequestDocumentation.parameterWithName("max_allowed_clip_overlap")
                .optional()
                .description("")
                .attributes(Attributes.key("type").value("String"))
        )
        RestAssured.given(hestuDocumentationSpec)
            .filter(
                document(
                    "highlights-get",
                    requestParameters,
                    responseFields,
                ),
            )
            .filter(
                RestAssuredRestDocumentationWrapper.document(
                    "{method-name}",
                    "Can get filtered highlights",
                    false,
                    responseFields,
                ),
            )
            .`when`()
            .get(HIGHLIGHTS_PATH).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
