package com.adyen.issuing.mobile.provisioning.exampleapp.mock

import com.adyen.issuing.mobile.provisioning.exampleapp.backend.Backend
import com.adyen.issuing.mobile.provisioning.exampleapp.data.CardActivationResult
import com.adyen.issuing.mobile.provisioning.exampleapp.data.OpaqueCardDataResponse

/**
 * Mock backend implementation.
 */
class MockBackend : Backend {

    /**
     * Request card activation data (`sdkInput`) by making a **GET** request to
     * `paymentInstruments/$[paymentInstrumentId]/networkTokenActivationData` endpoint.
     */
    override fun requestCardActivation(paymentInstrumentId: String): CardActivationResult =
        CardActivationResult.Active(dummySdkInput)

    /**
     * Request opaque payment card data by making **POST** request to
     * `paymentInstruments/$[paymentInstrumentId]/networkTokenActivationData` endpoint and passing
     * [sdkOutput] in the body.
     */
    override fun requestOpaquePaymentCardData(
        paymentInstrumentId: String,
        sdkOutput: String
    ): OpaqueCardDataResponse =
        OpaqueCardDataResponse("dummy opaque data")

    companion object {
        private val dummySdkInput: String =
            """eyJwYXltZW50SW5zdHJ1bWVudElkIjoiUEFZTUVOVF9JTlNUUlVNRU5UX0lEIiwiY2FyZGhvb
            GRlck5hbWUiOiJKb2hubnkgQXBwbGVzZWVkIiwiYnJhbmQiOiJ2aXNhIiwibGFzdEZvdXIiOiI4OTUyIiwic3
            VwcG9ydGVkV2FsbGV0cyI6WyJnb29nbGVQYXkiLCJhcHBsZVBheSJdfQ=="""
                .trimIndent().replace(Regex("""(\r\n)|\n"""), "")
    }
}

