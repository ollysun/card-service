package com.vayapay.cardidentification.core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRangeJsonModel
import com.vayapay.cardidentification.model.CardRequestDto
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.*


@Service
class CardIdentificationService constructor(val cardDataStorage: CardDataService) {

    private val logger = KotlinLogging.logger {}


    @Value("\${card-storage.ptoId}")
    lateinit var ptoid: String

    @Value("\${bin.locationBinRangeFile}")
    lateinit var binRangeLocation: String

    suspend fun saveCardStorage(cardDataRequest: CardRequestDto): StoreAndLinkCardDataResponse {

        val pan: String = cardDataRequest.cardData.pan.trim()
        logger.info { println("validating bin range pan " + validationPanBinRange(pan)) }
        // validate digitnumber, luhn algorithm check and binranges
        if (!isDigitNumber(pan) || !luhnCheck(pan) || !validationPanBinRange(pan)) {
            throw CardIdentificationException("wrong pan number")
        }

        val otherCardData = CardData(cardDataRequest.cardData.pan, cardDataRequest.cardData.expirationDate)
        val cardDataList = mutableListOf(otherCardData)
        cardDataList.add(otherCardData)
        if (!cardDataRequest.bankAccountNumber.isNullOrEmpty()) {
            val bankAxeptPan = BIN_BANK_AXEPT.plus(cardDataRequest.bankAccountNumber)
            val bankAxeptCardData = CardData(bankAxeptPan, cardDataRequest.cardData.expirationDate)
            cardDataList.add(bankAxeptCardData)
        }

        return cardDataStorage.storeCardData(ptoid, cardDataList)!!
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

        return checksum % 10 == 0
    }

    fun isDigitNumber(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    fun validationPanBinRange(pan: String): Boolean {
        val mapper = jacksonObjectMapper()
        mapper.findAndRegisterModules();
        val typeReference: TypeReference<List<BinRangeJsonModel>> = object : TypeReference<List<BinRangeJsonModel>>() {}
        val cpr = FileReader(File(binRangeLocation).absolutePath)
        logger.info { "file location " + File(binRangeLocation).absolutePath  }
        val reader = BufferedReader(cpr)
        val binRanges: List<BinRangeJsonModel> =
            mapper.readValue(FileCopyUtils.copyToString(reader), typeReference) as List<BinRangeJsonModel>
        val panSixDigit = pan.take(SIX_DIGIT);

        return binRanges.asSequence().filter {
            panSixDigit == it.binRangeFrom.take(SIX_DIGIT) && panSixDigit == it.binRangeTo.take(SIX_DIGIT) }.any()

    }


}