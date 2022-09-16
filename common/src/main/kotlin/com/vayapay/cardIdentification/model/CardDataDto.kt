package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class CardDataDto(
                    @field: NotBlank(message ="pan number is mandatory")
                    @field: Size(min=13, max=19, message = "pan must be of length between 13 and 19")
                    val pan: String,
                    @field: NotBlank(message ="expiration date is mandatory")
                    val expirationDate: String)
