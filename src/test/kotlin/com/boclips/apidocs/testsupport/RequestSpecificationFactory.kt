package com.boclips.apidocs.testsupport

import com.boclips.apidocs.testsupport.preprocessors.AuthHeaderMaskingPreprocessor.Companion.maskAuthHeader
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import java.util.regex.Pattern

class RequestSpecificationFactory {
    companion object {
        fun createFor(
            accessToken: String,
            restDocumentation: RestDocumentationContextProvider,
            bearerTokenDocumentationPolicy: BearerTokenDocumentationPolicy = BearerTokenDocumentationPolicy.MASK,
            baseUri: String = "https://api.staging-boclips.com",
        ): RequestSpecification =
            RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addHeader("Authorization", "Bearer $accessToken")
                .addFilter(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(
                            modifyUris()
                                .scheme("https")
                                .host("api.boclips.com"),
                            handleBearerToken(bearerTokenDocumentationPolicy),
                            replacePattern(Pattern.compile("staging-boclips"), "boclips"),
                            prettyPrint(),
                        ).withResponseDefaults(
                            StagingUriModifyingOperationPreprocessor(),
                            modifyHeaders()
                                .remove("Authorization")
                                .remove("Set-Cookie")
                                .remove("Date")
                                .remove("Expires")
                                .remove("Pragma")
                                .remove("X-Frame-Options")
                                .remove("X-Content-Type-Options")
                                .remove("X-Xss-Protection")
                                .remove("Strict-Transport-Security")
                                .remove("Cache-Control")
                                .remove("Transfer-Encoding"),
                            replacePattern(Pattern.compile("staging-boclips"), "boclips"),
                            prettyPrint(),
                        ),
                ).build()

        private fun handleBearerToken(policy: BearerTokenDocumentationPolicy): OperationPreprocessor =
            when (policy) {
                BearerTokenDocumentationPolicy.HIDE -> modifyHeaders().remove("Authorization")
                BearerTokenDocumentationPolicy.MASK -> maskAuthHeader()
            }
    }
}

enum class BearerTokenDocumentationPolicy {
    HIDE,
    MASK,
}
