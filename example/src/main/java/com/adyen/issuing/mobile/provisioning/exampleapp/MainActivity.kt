package com.adyen.issuing.mobile.provisioning.exampleapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.adyen.issuing.mobile.provisioning.exampleapp.ui.MainScreen
import com.adyen.issuing.mobile.provisioning.exampleapp.ui.theme.ExampleAppTheme
import com.adyen.issuing.mobile.provisioning.exampleapp.viewmodel.MainViewModel
import com.adyen.issuing.mobile.provisioning.exampleapp.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory { this }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val cardState by viewModel.cardState.collectAsState()
            ExampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        card = cardState,
                        lastFour = "1234",
                        onAddToWalletClicked = viewModel::provisionCard
                    )
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!viewModel.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
