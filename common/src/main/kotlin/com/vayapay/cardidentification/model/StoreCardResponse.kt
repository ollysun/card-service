package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

//TODO To remove this class
@Serializable
data class StoreCardResponse(val cardId: CardIdResponse,
                             val errorCode: String,
                             val errorMessage: String)
