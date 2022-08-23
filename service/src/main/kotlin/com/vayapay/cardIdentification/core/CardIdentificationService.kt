package com.vayapay.cardIdentification.core

import com.vayapay.cardIdentification.CardDataStorageClient
import com.vayapay.cardIdentification.exception.CardIdentificationException
import com.vayapay.cardIdentification.model.CardIdResponse
import com.vayapay.cardIdentification.model.CardRequestDto
import com.vayapay.cardIdentification.model.StoreCardResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class CardIdentificationService {

    lateinit var cardStorageClient: CardDataStorageClient

    suspend fun saveCardStorage(cardDataRequest: CardRequestDto): StoreCardResponse {

        val pan: String = cardDataRequest.cardData.pan.trim()

        if (!isDigitNumber(pan) || !luhmCheck(pan)) {
            throw CardIdentificationException("wrong pan number")
        }
        //val storeCardResponse : CardIdResponse? = cardStorageClient.storeCardData(cardDataRequest.id,cardDataRequest.cardData)

        val cardIdResponse = CardIdResponse("visa", UUID.randomUUID(), null)
        return StoreCardResponse(cardIdResponse, "", "")
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

        return checksum % 10 == 0
    }

    fun isDigitNumber(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}