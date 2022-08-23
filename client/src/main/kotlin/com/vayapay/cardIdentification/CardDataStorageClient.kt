package com.vayapay.cardIdentification

import com.vayapay.cardIdentification.model.CardData
import com.vayapay.cardIdentification.model.CardIdResponse


interface CardDataStorageClient {

    /**
     * Persist [CardData] of `ptoId` and return [CardIdResponse]. If a failure occurred while tokenizing
     * [CardData] null will be returned.
     */
    suspend fun storeCardData(ptoId: String, cardData: CardData): CardIdResponse?
}