package com.boclips.apidocs.testsupport

import com.boclips.apidocs.testsupport.preprocessors.AuthHeaderMaskingPreprocessor.Companion.maskAuthHeader
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration
import java.util.regex.Pattern

class RequestSpecificationFactory {
    companion object {
        fun createFor(accessToken: String, restDocumentation: RestDocumentationContextProvider): RequestSpecification {
            return RequestSpecBuilder()
                .setBaseUri("https://api.staging-boclips.com/v1")
                .addHeader("Authorization", "Bearer $accessToken")
                .addFilter(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(
                            modifyUris()
                                .scheme("https")
                                .host("api.boclips.com"),
                            maskAuthHeader(),
                            replacePattern(Pattern.compile("staging-boclips"), "boclips"),
                            prettyPrint()
                        )
                        .withResponseDefaults(
                            modifyUris()
                                .scheme("https")
                                .host("api.boclips.com"),
                            removeHeaders(
                                "Authorization",
                                "Set-Cookie",
                                "Date",
                                "Expires",
                                "Pragma",
                                "X-Frame-Options",
                                "X-Content-Type-Options",
                                "X-Xss-Protection",
                                "Strict-Transport-Security",
                                "Cache-Control",
                                "Transfer-Encoding"
                            ),
                            replacePattern(Pattern.compile("staging-boclips"), "boclips"),
                            prettyPrint()
                        )
                ).build()
        }
    }
}