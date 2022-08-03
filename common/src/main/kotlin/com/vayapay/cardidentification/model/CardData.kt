package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class CardData(
                    @field: NotBlank(message ="pan number is mandatory")
                    @field: Size(max=17, message = "please enter seventeen digit")
                    val pan: String,
                    @field: NotBlank(message ="expiration date is mandatory")
                    val expirationDate: String)
