package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreCardDataRequest(
    val ptoId : String,
    val cardData: CardData
)