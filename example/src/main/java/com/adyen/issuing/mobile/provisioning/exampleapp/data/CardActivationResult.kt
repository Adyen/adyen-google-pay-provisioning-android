/*
 * Copyright (c) 2025 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by davidw on 8/5/2024.
 */

package com.adyen.issuing.mobile.provisioning.exampleapp.data

sealed interface CardActivationResult {
    data class Active(val sdkInput: String) : CardActivationResult
    data object Disabled : CardActivationResult
}