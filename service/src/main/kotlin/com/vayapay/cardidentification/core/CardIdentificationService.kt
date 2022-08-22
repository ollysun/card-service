package com.vayapay.cardidentification.core

import com.vayapay.cardidentification.client.RSocketCardDataClient
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.messages.StoreCardDataResponse
import com.vayapay.cardidentification.model.CardData
import com.vayapay.cardidentification.model.CardRequestDto
import com.vayapay.cardidentification.model.StoreCardDataRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CardIdentificationService constructor( val cardDataClient: RSocketCardDataClient)  {


    fun saveCardStorage(cardDataRequest: CardRequestDto): Mono<StoreCardDataResponse> {

        val pan: String = cardDataRequest.cardData.pan.trim()

        if (!isDigitNumber(pan) || !luhmCheck(pan)) {
            throw CardIdentificationException("wrong pan number")
        }

        val cardData = CardData(cardDataRequest.cardData.pan, cardDataRequest.cardData.expirationDate)
        val storeCardDataRequest = StoreCardDataRequest(cardData);

        // what will l place on ptoID
        return cardDataClient.storeCardData(storeCardDataRequest)
    }

    fun luhmCheck(number: String): Boolean {
        var checksum: Int = 0

        for (i in number.length - 1 downTo 0 step 2) {
            checksum += number[i] - '0'
        }
        for (i in number.length - 2 downTo 0 step 2) {
            val n: Int = (number[i] - '0') * 2
            checksum += if (n > 9) n - 9 else n
        }

        return checksum%10 == 0
    }

    fun isDigitNumber(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}