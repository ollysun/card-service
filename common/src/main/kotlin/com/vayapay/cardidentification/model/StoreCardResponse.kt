package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreCardResponse(val cardId: CardIdResponse,
                             val errorCode: String,
                             val errorMessage: String)