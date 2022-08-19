package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank

@Serializable
data class CardRequestDto(
                          val cardData : CardData,
                          val bankAccountNumber: String? = null)
