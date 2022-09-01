package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreAndLinkCardDataResponse(val cardId: List<CardId>,
                                        val errorCode: String,
                                        val errorMessage: String)
