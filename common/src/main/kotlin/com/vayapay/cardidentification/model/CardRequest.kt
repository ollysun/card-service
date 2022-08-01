package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

@Serializable
data class CardRequest(val id: String,
                       val cardData : CardData)
