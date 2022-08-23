package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class CardFormData(
    @NotBlank(message = "Card number is mandatory")
    @Size(min = 13, max = 19, message = "Card number must be of length between 13 and 19")
    val cardNumber: String = "",
    @NotBlank(message = "Expiry month is mandatory")
    @Size(min = 2, max = 2, message = "expiry month must be of length 2")
    val expiryMonth: String = "",
    @NotBlank(message = "expiry year is mandatory")
    @Size(min = 2, max = 2, message = "expiry year must be of length 2")
    val expiryYear: String = "",
    @NotBlank(message = "Account number is mandatory")
    val accountNumber: String? = null
)