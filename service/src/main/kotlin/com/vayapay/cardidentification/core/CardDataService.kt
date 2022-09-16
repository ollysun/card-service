package com.vayapay.cardidentification.core

import com.vayapay.carddata.client.CardDataClient
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.messages.StoreAndLinkCardDataRequest
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CardDataService (val cardDataClient: CardDataClient) {

    private val logger = KotlinLogging.logger {}

    suspend fun storeCardData(ptoId: String, cardDataList: List<CardData>): StoreAndLinkCardDataResponse? {
        val cardIdData = cardDataClient.storeAndLinkCardData(ptoId, cardDataList)
        logger.info { "Card data retrieved from Card Data Storage service:  $cardIdData" }

        if (cardIdData.isEmpty()) {
            logger.error { "card data cannot be saved to card storage" }
            return null
        }
        return StoreAndLinkCardDataResponse(cardIdData)
    }
}