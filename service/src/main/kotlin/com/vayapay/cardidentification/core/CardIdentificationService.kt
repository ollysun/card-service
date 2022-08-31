package com.vayapay.cardidentification.core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.messages.StoreCardDataResponse
import com.vayapay.cardidentification.model.*
import mu.KotlinLogging
import mu.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.IOException

import java.io.InputStream




@Service
class CardIdentificationService constructor( val cardDataClient: RSocketCardDataClient)  {

    private val logger = KotlinLogging.logger {}


    @Value("\${card-storage.ptoId}")
    lateinit var ptoid : String;
    fun saveCardStorage(cardDataRequest: CardRequestDto): Mono<StoreCardDataResponse> {

        val pan: String = cardDataRequest.cardData.pan.trim()

        // validate digitnumber, luhn algorithm check and binranges
        if (!isDigitNumber(pan) || !luhmCheck(pan) || !validationPanBinRange(pan)) {
            throw CardIdentificationException("wrong pan number")
        }

        val bankAxemptPan = cardDataRequest.cardData.pan.take(6).plus(cardDataRequest.bankAccountNumber)
        val otherCardData = CardData(cardDataRequest.cardData.pan, cardDataRequest.cardData.expirationDate)
        val bankAxemptCardData = CardData(bankAxemptPan, cardDataRequest.cardData.expirationDate)
        val cardDataList = ArrayList<CardData>()
        cardDataList.add(bankAxemptCardData)
        cardDataList.add(otherCardData)
        val storeCardDataRequest = StoreCardDataRequest(ptoid, cardDataList);

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

    fun validationPanBinRange(pan : String) : Boolean{
        val mapper = ObjectMapper()
        val typeReference: TypeReference<List<BinRangeJsonModel>> = object : TypeReference<List<BinRangeJsonModel>>() {}
        val inputStream: InputStream = TypeReference::class.java.getResourceAsStream("/data/BinRange.json") as InputStream
        try {
            val users: List<BinRangeJsonModel> = mapper.readValue(inputStream, typeReference) as List<BinRangeJsonModel>
            val panSixDigit = pan.take(6);
            users.forEach{
                   if(panSixDigit != it.binRangeFrom.take(6) && panSixDigit != it.binRangeTo.take(6)) {
                       return false
                   }
            }
        } catch (e: IOException) {
            logger("Unable to save users: " + e.message)
        }

        return true
    }
}