package com.vayapay.cardIdentification.model

import kotlinx.serialization.Serializable

@Serializable
data class CardRequestDto(val cardData: CardDataDto, val bankAccountNumber: String? = null)
