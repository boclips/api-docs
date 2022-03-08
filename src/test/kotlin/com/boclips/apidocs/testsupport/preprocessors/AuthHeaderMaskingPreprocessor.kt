package com.boclips.apidocs.testsupport.preprocessors

import org.springframework.http.HttpHeaders.writableHttpHeaders
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationRequestFactory
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

class AuthHeaderMaskingPreprocessor : OperationPreprocessor {
    companion object {
        fun maskAuthHeader() = AuthHeaderMaskingPreprocessor()
    }

    private val requestFactory = OperationRequestFactory()

    override fun preprocess(request: OperationRequest): OperationRequest {
        return requestFactory.createFrom(
            request,
            writableHttpHeaders(request.headers).apply { setBearerAuth("***") }
        )
    }

    override fun preprocess(response: OperationResponse): OperationResponse {
        return response
    }
}
