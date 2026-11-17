package com.boclips.apidocs.testsupport

import org.springframework.http.HttpHeaders
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.OperationResponseFactory
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

class StagingUriModifyingOperationPreprocessor : OperationPreprocessor {
    override fun preprocess(request: OperationRequest): OperationRequest = request

    override fun preprocess(response: OperationResponse): OperationResponse =
        OperationResponseFactory().createFrom(response, modify(response.headers))

    private fun modify(headers: HttpHeaders): HttpHeaders =
        HttpHeaders().apply {
            headers.forEach { key, values ->
                values.forEach { value -> add(key, value.replace("staging-boclips", "boclips")) }
            }
        }
}
