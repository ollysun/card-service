package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

@Serializable
data class CardRequestDto(
                          val cardData : CardData,
                          val bankAccountNumber: String? = null)
