package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class CardFormData(
    @field: NotBlank(message = "Card number is mandatory")
    @field: Size(min = 13, max = 19, message = "Card number must be of length between 13 and 19")
    val cardNumber: String = "",
    @field: NotBlank(message = "Expiry month is mandatory")
    @field: Size(min = 2, max = 2, message = "expiry month must be of length 2")
    val expiryMonth: String = "",
    @field: NotBlank(message = "expiry year is mandatory")
    @field: Size(min = 2, max = 2, message = "expiry year must be of length 2")
    val expiryYear: String = "",
    @field: NotBlank(message = "Account number is mandatory")
    val accountNumber: String? = null
) {
    constructor() : this("", "", "", null)
}