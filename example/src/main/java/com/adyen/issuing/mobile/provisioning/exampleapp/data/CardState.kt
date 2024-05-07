package com.adyen.issuing.mobile.provisioning.exampleapp.data

sealed interface CardState {
    data object Loading : CardState
    data object NotAddedToWallet : CardState
    data object AddedToWallet : CardState
    data object NotSupported : CardState
    data object Disabled : CardState
    data class Error(val message: String?) : CardState
}