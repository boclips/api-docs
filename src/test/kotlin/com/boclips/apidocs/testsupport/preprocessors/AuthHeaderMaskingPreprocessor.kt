package com.boclips.apidocs.testsupport.preprocessors

import org.springframework.http.HttpHeaders
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationRequestFactory
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

class AuthHeaderMaskingPreprocessor : OperationPreprocessor {
    companion object {
        fun maskAuthHeader() = AuthHeaderMaskingPreprocessor()
    }

    private val requestFactory = OperationRequestFactory()

    override fun preprocess(request: OperationRequest): OperationRequest =
        requestFactory.createFrom(
            request,
            HttpHeaders.copyOf(request.headers).apply { setBearerAuth("***") },
        )

    override fun preprocess(response: OperationResponse): OperationResponse = response
}
