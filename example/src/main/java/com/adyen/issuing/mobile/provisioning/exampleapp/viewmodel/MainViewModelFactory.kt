package com.adyen.issuing.mobile.provisioning.exampleapp.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adyen.issuing.mobile.provisioning.exampleapp.backend.Backend
import com.adyen.issuing.mobile.provisioning.exampleapp.mock.MockBackend

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val paymentInstrumentId: String = "PAYMENT_INSTRUMENT_ID",
    private val backend: Backend = MockBackend(),
    private val activityProvider: () -> Activity,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel( paymentInstrumentId, backend, activityProvider) as T
    }
}