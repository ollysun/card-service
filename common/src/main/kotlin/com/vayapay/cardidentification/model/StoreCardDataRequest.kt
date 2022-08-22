package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable
// ToDo To remove this class
@Serializable
data class StoreCardDataRequest(
    val cardData: CardData
)