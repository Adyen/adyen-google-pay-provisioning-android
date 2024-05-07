package com.adyen.issuing.mobile.provisioning.exampleapp.data

sealed interface CardActivationResult {
    data class Active(val sdkInput: String) : CardActivationResult
    data object Disabled : CardActivationResult
}