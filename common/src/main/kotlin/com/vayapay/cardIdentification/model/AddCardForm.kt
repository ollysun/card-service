package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.CreditCardNumber
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Serializable
data class AddCardForm(
    @field:CreditCardNumber
    @field:NotEmpty(message = "Card number is mandatory")
    @field:Size(min = 13, max = 19, message = "Card number must be between 13 and 19 digits")
    val cardNumber: String,

    @field:NotEmpty(message = "Expiry month is mandatory")
    @field:Size(min = 2, max = 2, message = "Expiry month must be of length 2")
    val expiryMonth: String,

    @field:NotEmpty(message = "Expiry year is mandatory")
    @field:Size(min = 2, max = 2, message = "Expiry year must be of length 2")
    val expiryYear: String,

    @field:Size(max = 11, message = "Account number must not be more than 11 digits")
    val accountNumber: String? = null
)