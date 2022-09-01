package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable
@Serializable
data class StoreAndLinkCardDataRequest(
    val ptoId:String,
    val cardData: List<CardData>
)