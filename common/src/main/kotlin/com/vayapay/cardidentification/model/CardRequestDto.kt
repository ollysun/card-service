package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class CardRequestDto(
                          @field: NotBlank(message ="id is mandatory")
                          val id: String,
                          val cardData : CardData,
                          val bankAccountNumber: String?)
