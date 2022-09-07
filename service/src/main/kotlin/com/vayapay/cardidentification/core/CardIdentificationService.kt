package com.vayapay.cardidentification.core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.messages.StoreAndLinkCardDataRequest
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRangeJsonModel
import com.vayapay.cardidentification.model.CardRequestDto
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


@Service
class CardIdentificationService constructor( val cardDataStorage: CardDataService)  {

    private val logger = KotlinLogging.logger {}

    @Value("\${card-storage.ptoId}")
    lateinit var ptoid : String;
    suspend fun saveCardStorage(cardDataRequest: CardRequestDto): StoreAndLinkCardDataResponse {

        val pan: String = cardDataRequest.cardData.pan.trim()

        logger.info { println("validating bin range pan " + validationPanBinRange(pan)) }
        // validate digitnumber, luhn algorithm check and binranges
        if (!isDigitNumber(pan) || !luhnCheck(pan) || !validationPanBinRange(pan)) {
            throw CardIdentificationException("wrong pan number")
        }

        val otherCardData = CardData(cardDataRequest.cardData.pan, cardDataRequest.cardData.expirationDate)
        val cardDataList = ArrayList<CardData>()
            cardDataList.add(otherCardData)
        if (!cardDataRequest.bankAccountNumber.isNullOrEmpty()){
            val bankAxeptPan = cardDataRequest.cardData.pan.take(6).plus(cardDataRequest.bankAccountNumber)
            val bankAxeptCardData = CardData(bankAxeptPan, cardDataRequest.cardData.expirationDate)
            cardDataList.add(bankAxeptCardData)
        }
        val storeAndLinkCardDataRequest = StoreAndLinkCardDataRequest(ptoid, cardDataList)

        return cardDataStorage.storeCardData(storeAndLinkCardDataRequest)
    }

    fun luhnCheck(number: String): Boolean {
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
        val mapper = jacksonObjectMapper()
        mapper.findAndRegisterModules();
        val typeReference: TypeReference<List<BinRangeJsonModel>> = object : TypeReference<List<BinRangeJsonModel>>() {}
        val cpr = ClassPathResource("BinRange.json")
        val inputStream: InputStream = cpr.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))

        val binRanges: List<BinRangeJsonModel> = mapper.readValue(FileCopyUtils.copyToString( reader ), typeReference) as List<BinRangeJsonModel>
        val panSixDigit = pan.take(6);

        return binRanges.asSequence().filter {
               panSixDigit == it.binRangeFrom.take(6) && panSixDigit == it.binRangeTo.take(6) }
               .any()

    }
}