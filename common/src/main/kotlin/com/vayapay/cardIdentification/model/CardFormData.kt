package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Serializable
class CardFormData(
    @NotEmpty(message = "Card number is mandatory")
    @Size(min = 13, max = 19, message = "Card number must be of length between 13 and 19")
    val cardNumber: String,
    @NotEmpty(message = "Expiry month is mandatory")
    @Size(min = 2, max = 2, message = "expiry month must be of length 2")
    val expiryMonth: String,
    @NotEmpty(message = "expiry year is mandatory")
    @Size(min = 2, max = 2, message = "expiry year must be of length 2")
    val expiryYear: String,
    @Size(max = 11, message = "Account number must not be more than 11")
    val accountNumber: String? = null
)