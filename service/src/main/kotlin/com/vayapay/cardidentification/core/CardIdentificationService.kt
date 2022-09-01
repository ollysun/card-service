package com.vayapay.cardidentification.core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.messages.StoreAndLinkCardDataRequest
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRangeJsonModel
import com.vayapay.cardidentification.model.CardRequestDto
import mu.KotlinLogging
import mu.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.IOException

import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths


@Service
class CardIdentificationService constructor( val cardDataStorage: CardDataService)  {

    private val logger = KotlinLogging.logger {}


    @Value("\${card-storage.ptoId}")
    lateinit var ptoid : String;
    suspend fun saveCardStorage(cardDataRequest: CardRequestDto): StoreAndLinkCardDataResponse? {

        val pan: String = cardDataRequest.cardData.pan.trim()

        // validate digitnumber, luhn algorithm check and binranges
        if (!isDigitNumber(pan) || !luhmCheck(pan) || !validationPanBinRange(pan)) {
            throw CardIdentificationException("wrong pan number")
        }
//        if (!isDigitNumber(pan)){
//            throw CardIdentificationException("we need numeric number")
//        }
//        if (!luhmCheck(pan)){
//            throw CardIdentificationException("luhm check algorithm not pass")
//        }
//
//        if(!validationPanBinRange(pan)){
//            throw CardIdentificationException("wrong pan for bin ranges")
//        }


        val otherCardData = CardData(cardDataRequest.cardData.pan, cardDataRequest.cardData.expirationDate)
        val cardDataList = ArrayList<CardData>()
            cardDataList.add(otherCardData)
        if (!cardDataRequest.bankAccountNumber.isNullOrEmpty()){
            val bankAxeptPan = cardDataRequest.cardData.pan.take(6).plus(cardDataRequest.bankAccountNumber)
            val bankAxeptCardData = CardData(bankAxeptPan, cardDataRequest.cardData.expirationDate)
            cardDataList.add(bankAxeptCardData)
        }

        val storeAndLinkCardDataRequest = StoreAndLinkCardDataRequest(ptoid, cardDataList);

        return cardDataStorage.storeCardData(storeAndLinkCardDataRequest)
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
        val mapper = jacksonObjectMapper()
        mapper.findAndRegisterModules();
        val typeReference: TypeReference<List<BinRangeJsonModel>> = object : TypeReference<List<BinRangeJsonModel>>() {}
        val path: Path = Paths.get( "data/BinRange.json").toAbsolutePath()
        val inputStream: InputStream = FileInputStream(path.toFile())
        var result = false
        try {
            val binRanges: List<BinRangeJsonModel> = mapper.readValue(inputStream, typeReference) as List<BinRangeJsonModel>
            val panSixDigit = pan.take(6);
            binRanges.asSequence().forEach {
                if (panSixDigit == it.binRangeFrom.take(6) && panSixDigit == it.binRangeTo.take(6)) {
                    result = true
                }
            }
        } catch (e: IOException) {
            logger("Unable to assess the binranges file: " + e.message)
        }

        return result
    }
}