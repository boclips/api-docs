package com.boclips.apidocs

import com.boclips.apidocs.testsupport.AbstractDocTests
import com.boclips.apidocs.testsupport.RequestSpecificationFactory
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.snippet.Attributes

class HestuDocTests : AbstractDocTests() {
    private val productionVideoId = "5c54d67ed8eafeecae2041c2"
    private val productionHighlightId = "CLPOTj69sapco"
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
    fun `Get highlight`(restDocumentation: RestDocumentationContextProvider) {
        val responseFields = responseFields(
            fieldWithPath("id").description("ID of the highlight"),
            fieldWithPath("video_id").description("ID of the highlight's video"),
            fieldWithPath("title").description("Title of the highlight"),
            fieldWithPath("caption").description("Captions in the highlight"),
            fieldWithPath("start_time").description("Start time of the highlight"),
            fieldWithPath("end_time").description("End  time of the highlight"),
            fieldWithPath("duration").description("Duration of the highlight"),
            fieldWithPath("score").description("Confidence score of the highlight"),
            subsectionWithPath("_links").description("HAL links for the highlight")

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
            .get(HIGHLIGHT_PATH, productionHighlightId).apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }

    @Test
    fun `Get highlights`(restDocumentation: RestDocumentationContextProvider) {
        val responseFields = responseFields(
            fieldWithPath("_embedded.highlights[].id").description("ID of the highlight"),
            fieldWithPath("_embedded.highlights[].video_id").description("ID of the highlight's video"),
            fieldWithPath("_embedded.highlights[].title").description("Title of the highlight"),
            fieldWithPath("_embedded.highlights[].caption").description("Captions in the highlight"),
            fieldWithPath("_embedded.highlights[].start_time").description("Start time of the highlight"),
            fieldWithPath("_embedded.highlights[].end_time").description("End  time of the highlight"),
            fieldWithPath("_embedded.highlights[].duration").description("Duration of the highlight"),
            fieldWithPath("_embedded.highlights[].score").description("Confidence score of the highlight"),
            subsectionWithPath("_embedded.highlights[]._links").description("HAL links for the highlight"),
        )
        val requestParameters = RequestDocumentation.requestParameters(
            RequestDocumentation.parameterWithName("query")
                .description("The text search query")
                .attributes(Attributes.key("type").value("String")),
            RequestDocumentation.parameterWithName("threshold")
                .optional()
                .description("Minimum similarity score a clip needs to be returned between 0 and 1. Default is none, which will return the closest match no matter what.")
                .attributes(Attributes.key("type").value("Number")),
            RequestDocumentation.parameterWithName("size")
                .optional()
                .description("The number of highlights to return, 100 by default")
                .attributes(Attributes.key("type").value("Number")),

            RequestDocumentation.parameterWithName("education_level")
                .optional()
                .description("Filter by education level. Multiple values can be specified (comma separated, or by repeating the parameter). See possible values at <<resources-education-levels,education levels resource>>")
                .attributes(Attributes.key("type").value("String")),

            RequestDocumentation.parameterWithName("max_overlap")
                .optional()
                .description("If multiple highlights come from the same video, this is the maximum they are allowed to overlap as a % (expressed as a decimal e.g. 0.2 is 20%) of the smallest video. If 1 a highlight can be a full segment of another highlight. If 0 no overlap is allowed.")
                .attributes(Attributes.key("type").value("String")),

            RequestDocumentation.parameterWithName("hyper_relevance")
                .optional()
                .description("If true, the highlights will start at the part that is most relevant to your query. Default is false, which will return the full duration of the highlights.")
                .attributes(Attributes.key("type").value("Boolean"))
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
                    requestParameters,
                    responseFields,
                ),
            )
            .`when`()
            .get("$HIGHLIGHTS_PATH?query=test")
            .apply { prettyPrint() }
            .then()
            .assertThat().statusCode(CoreMatchers.`is`(200))
    }
}
