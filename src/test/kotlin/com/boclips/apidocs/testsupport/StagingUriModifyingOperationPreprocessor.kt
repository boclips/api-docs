package com.boclips.apidocs.testsupport

import org.springframework.http.HttpHeaders
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.OperationResponseFactory
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor
import org.springframework.restdocs.operation.preprocess.UriModifyingOperationPreprocessor

class StagingUriModifyingOperationPreprocessor : OperationPreprocessor {
    override fun preprocess(request: OperationRequest?): OperationRequest? {
        return request
    }

    override fun preprocess(response: OperationResponse?): OperationResponse? {
        if (response == null) {
            return response
        }

        return OperationResponseFactory().create(response.status.value(), modify(response.headers), response.content)
    }

    private fun modify(headers: HttpHeaders): HttpHeaders? {
        val modified = HttpHeaders()
        for ((key, values) in headers) {
            for (value in values) {
                modified.add(key, value.replace("staging-boclips", "boclips"))
            }
        }

        return modified
    }
}
