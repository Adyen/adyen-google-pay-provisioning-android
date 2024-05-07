package com.adyen.issuing.mobile.provisioning.exampleapp.backend

import com.adyen.issuing.mobile.provisioning.exampleapp.data.CardActivationResult
import com.adyen.issuing.mobile.provisioning.exampleapp.data.OpaqueCardDataResponse

interface Backend {

    /**
     * Request card activation data (`sdkInput`) by making a **GET** request to
     * `paymentInstruments/$[paymentInstrumentId]/networkTokenActivationData` endpoint.
     */
    fun requestCardActivation(paymentInstrumentId: String): CardActivationResult

    /**
     * Request opaque payment card data by making **POST** request to
     * `paymentInstruments/$[paymentInstrumentId]/networkTokenActivationData` endpoint and passing
     * [sdkOutput] in the body.
     */
    fun requestOpaquePaymentCardData(
        paymentInstrumentId: String,
        sdkOutput: String
    ): OpaqueCardDataResponse
}