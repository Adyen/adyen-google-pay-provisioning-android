package com.adyen.issuing.mobile.provisioning.exampleapp.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.issuing.mobile.provisioning.client.CardProvisioning
import com.adyen.issuing.mobile.provisioning.client.annotation.AdyenExperimentalApi
import com.adyen.issuing.mobile.provisioning.client.data.CardAddress
import com.adyen.issuing.mobile.provisioning.client.results.CanProvisionResult
import com.adyen.issuing.mobile.provisioning.client.results.CardProvisioningCreateResult
import com.adyen.issuing.mobile.provisioning.client.results.GetSdkOutputResult
import com.adyen.issuing.mobile.provisioning.client.results.ProvisionResult
import com.adyen.issuing.mobile.provisioning.exampleapp.backend.Backend
import com.adyen.issuing.mobile.provisioning.exampleapp.data.CardActivationResult
import com.adyen.issuing.mobile.provisioning.exampleapp.data.CardState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(AdyenExperimentalApi::class)
class MainViewModel(
    private val paymentInstrumentId: String,
    private val backend: Backend,
    private val activityProvider: () -> Activity,
) : ViewModel() {

    private var cardProvisioning: CardProvisioning? = null

    private val mutableCardState = MutableStateFlow<CardState>(CardState.Loading)
    val cardState: StateFlow<CardState> = mutableCardState.asStateFlow()

    private val mutableErrorEventChannel = Channel<String>()

    init {
        fetchState()
    }

    private fun fetchState() {
        viewModelScope.launch {
            val cardState = backend.requestCardActivation(paymentInstrumentId).let { cardActivationResult ->
                when (cardActivationResult) {
                    is CardActivationResult.Active -> CardProvisioning.create(
                        cardActivationResult.sdkInput, activityProvider
                    ).let { provisioningCreateResult ->
                        when (provisioningCreateResult) {
                            is CardProvisioningCreateResult.Success -> {
                                cardProvisioning = provisioningCreateResult.cardProvisioning
                                getCardState()
                            }
                            is CardProvisioningCreateResult.Failed.GooglePayNotSupported -> CardState.NotSupported
                            is CardProvisioningCreateResult.Failed.InvalidSdkInput -> CardState.Error(message = "Invalid SDK Input")
                        }
                    }
                    CardActivationResult.Disabled -> CardState.Disabled
                }
            }
            mutableCardState.emit(cardState)
        }
    }

    private suspend fun getCardState(): CardState =
        cardProvisioning?.canProvision()?.let {
            when (it) {
                is CanProvisionResult.CanBeProvisioned -> CardState.NotAddedToWallet
                is CanProvisionResult.CannotBeProvisioned.AlreadyExistsInWallet -> CardState.AddedToWallet
                is CanProvisionResult.CannotBeProvisioned.Error -> CardState.Error(message = it.throwable.message)
                is CanProvisionResult.CannotBeProvisioned.UnknownFailure -> CardState.Error(message = "Unknown failure")
            }

        } ?: CardState.Error(message = "Provisioning client not set")

    fun provisionCard(
        cardholderName: String = "John Doe",
        cardAddress: CardAddress = CardAddress()
    ) {
            viewModelScope.launch {
                cardProvisioning?.getSdkOutput()?.let {
                    val state = when (it) {
                        is GetSdkOutputResult.Failure -> CardState.Error("Failed to get sdk output data")
                        is GetSdkOutputResult.Success -> {
                            val opcResponse = backend.requestOpaquePaymentCardData(
                                paymentInstrumentId,
                                it.sdkOutput
                            )
                            cardProvisioning?.provision(
                                opcResponse.sdkInput,
                                cardholderName,
                                cardAddress
                            )
                                ?.let { provisioningResult ->
                                    when (provisioningResult) {
                                        is ProvisionResult.Success -> CardState.AddedToWallet
                                        else -> CardState.Error("Failed to provision card $provisioningResult")
                                    }
                                }
                        }
                    } ?: CardState.Error("Provisioning Failed")
                    mutableCardState.emit(state)
                }
            }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean =
        cardProvisioning?.onActivityResult(requestCode, resultCode, data) ?: false
}