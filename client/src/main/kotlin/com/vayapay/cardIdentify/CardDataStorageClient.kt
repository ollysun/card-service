package com.vayapay.cardIdentify

import com.vayapay.cardidentification.model.CardData
import com.vayapay.cardidentification.model.CardIdResponse


interface CardDataStorageClient {

    /**
     * Persist [CardData] of `ptoId` and return [CardIdResponse]. If a failure occurred while tokenizing
     * [CardData] null will be returned.
     */
    suspend fun storeCardData(ptoId: String, cardData: CardData): CardIdResponse?
}