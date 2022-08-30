package com.vayapay.cardidentification.core

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vayapay.cardidentification.exception.BadRequestException
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRange
import com.vayapay.cardidentification.model.BinRangeUploadModel
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Predicate
import java.util.stream.Collectors

@Service
class BinRangeService(
    val binRangeConfiguration:  BinRangeConfiguration) {

    suspend fun uploadBinRangesFile(file: MultipartFile): List<BinRange> {
        if (file.isEmpty)
            throw BadRequestException("Empty file")

        try {
            BufferedReader(InputStreamReader(file.inputStream)).use {
                val csvToBean = createBinRangeToBean(it)
                return processAndValidateBinRange(csvToBean)
            }
        } catch (ex: Exception) {
            throw CardIdentificationException("Error during csv import")
        }
    }

    private fun createBinRangeToBean(fileReader: BufferedReader?): CsvToBean<BinRangeUploadModel> =
        CsvToBeanBuilder<BinRangeUploadModel>(fileReader)
            .withType(BinRangeUploadModel::class.java)
            .withSkipLines(1) // this is to skip the header
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private suspend fun processAndValidateBinRange(csvBean : CsvToBean<BinRangeUploadModel>) : ArrayList<BinRange> {
        val binRangeUploadModelList : List<BinRangeUploadModel> = csvBean.toList()
        // filter for only non-prepaid accounting funding source
        val nonPrepaidPredicate: Predicate<BinRangeUploadModel> = Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.PREPAID) == d.accountFundingSource } // filter for only non-prepaid accounting funding source
        // check for countryCode
        val countryPredicate: Predicate<BinRangeUploadModel> = Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.COUNTRY_CODE) == d.countryCode }
        // check for currencyCode
        val currencyCodePredicate: Predicate<BinRangeUploadModel> = Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.CURRENCY_CODE) == d.currencyCode }
        // filter for only non-credit
        val nonCreditPredicate: Predicate<BinRangeUploadModel> = Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.CREDIT) != d.creditOrDebit }

        val filterList = binRangeUploadModelList.stream()
            .filter(nonCreditPredicate.and(nonPrepaidPredicate).and(countryPredicate).and(currencyCodePredicate))
            .collect(Collectors.toList())
        val binList =  arrayListOf<BinRange>()
        var count = 0
        for(bin in filterList){
            ++count
            val binRange = BinRange(
                count,
                mergeExtraDigit(bin.accountRangeLow!!),
                mergeExtraDigit(bin.accountRangeHigh!!),
                bin.cardScheme?.let { binRangeConfiguration.getCardScheme(it) }!!)
            binList.add(binRange)
        }

        processAndReturnJson(binList)
        return binList

    }

    private fun processAndReturnJson( binRangeList: List<BinRange>){
        try {
            val mapper = jacksonObjectMapper()
            mapper.findAndRegisterModules();
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // to serialize date
            val path: Path = Paths.get( "data/BinRange.json").toAbsolutePath()
            FileOutputStream(path.toString()).use {
                val strToBytes: ByteArray = mapper.writeValueAsString(binRangeList).toByteArray()
                it.write(strToBytes)
            }
        }catch (ex: Exception){
            throw CardIdentificationException(ex.message!!)
        }
    }

    private fun mergeExtraDigit(text: String) =
        text.plus(text.takeLast(4))

}