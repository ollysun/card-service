package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable

@Serializable
data class CardFormData(
    val cardNumber: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val accountNumber: String? = null
)