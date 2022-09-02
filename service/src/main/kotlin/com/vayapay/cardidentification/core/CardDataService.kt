package com.vayapay.cardidentification.core

import com.vayapay.carddata.client.CardDataClient
import com.vayapay.carddata.messages.StoreAndLinkCardDataRequest
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.exception.CardIdentificationException
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CardDataService constructor(val cardDataClient: CardDataClient){

    private val logger = KotlinLogging.logger {}

    suspend fun storeCardData(storeAndLinkCardDataRequest: StoreAndLinkCardDataRequest): StoreAndLinkCardDataResponse {
        val cardIdData  = cardDataClient.storeAndLinkCardData(storeAndLinkCardDataRequest.ptoId,
        storeAndLinkCardDataRequest.cardData)
        logger.info { "Card data retrieved from Card Data Storage service:  $cardIdData" }

        if (cardIdData.isEmpty()) {
            logger.error { "card data cannot be saved to card storage" }
            throw CardIdentificationException("card data cannot be saved to card storage")
        }
        return StoreAndLinkCardDataResponse(cardIdData)
    }
}