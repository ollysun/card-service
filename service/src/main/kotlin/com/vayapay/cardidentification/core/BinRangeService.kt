package com.vayapay.cardidentification.core

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vayapay.cardidentification.exception.BadRequestException
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRangeModel
import com.vayapay.cardidentification.model.BinRangeUploadModel
import mu.KotlinLogging
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
class BinRangeService(val binRangeConfiguration: BinRangeConfiguration) {

    private val logger = KotlinLogging.logger {}

    fun uploadBinRangesFile(file: MultipartFile): String {
        if (file.isEmpty)
            throw BadRequestException("Empty file")

        try {
            BufferedReader(InputStreamReader(file.inputStream)).use {
                val csvToBean = createBinRangeToBean(it)
                return processAndValidateBinRange(csvToBean)
            }
        } catch (ex: Exception) {
            logger.error { ex.localizedMessage }
            throw CardIdentificationException(ex.stackTraceToString())
        }
    }

    private fun createBinRangeToBean(fileReader: BufferedReader?): CsvToBean<BinRangeUploadModel> =
        CsvToBeanBuilder<BinRangeUploadModel>(fileReader)
            .withType(BinRangeUploadModel::class.java)
            .withSkipLines(1) // this is to skip the header
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun processAndValidateBinRange(csvBean: CsvToBean<BinRangeUploadModel>): String {
        val binRangeUploadModelList: List<BinRangeUploadModel> = csvBean.toList()
        // filter for only non-prepaid accounting funding source
        val nonPrepaidPredicate: Predicate<BinRangeUploadModel> =
            Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(Clauses.PREPAID) != d.accountFundingSource } // filter for only non-prepaid accounting funding source
        // check for countryCode
        val countryPredicate: Predicate<BinRangeUploadModel> =
            Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(Clauses.COUNTRY_CODE) == d.countryCode }
        // check for currencyCode
        val currencyCodePredicate: Predicate<BinRangeUploadModel> =
            Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(Clauses.CURRENCY_CODE) == d.currencyCode }
        // filter for only non-credit
        val nonCreditPredicate: Predicate<BinRangeUploadModel> =
            Predicate<BinRangeUploadModel> { d -> binRangeConfiguration.getBinValidation(Clauses.CREDIT) != d.creditOrDebit }

        val filterList = binRangeUploadModelList.stream()
            .filter(nonCreditPredicate.and(nonPrepaidPredicate).and(countryPredicate).and(currencyCodePredicate))
            .collect(Collectors.toList())
        val binList = arrayListOf<BinRangeModel>()
        var count = 0
        for (bin in filterList) {
            ++count
            val binRangeModel = BinRangeModel(
                count,
                mergeExtraDigit(bin.accountRangeLow!!),
                mergeExtraDigit(bin.accountRangeHigh!!),
                binRangeConfiguration.getCardScheme(bin.cardScheme!!)!!
            )
            binList.add(binRangeModel)
        }

        return processAndReturnJson(binList)

    }

    private fun processAndReturnJson(binRangeModelList: List<BinRangeModel>): String {
        try {
            val mapper = jacksonObjectMapper()
            mapper.findAndRegisterModules()
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // to serialize date
            val path: Path = Paths.get("data/BinRange.json").toAbsolutePath()
            FileOutputStream(path.toString()).use {
                val strToBytes: ByteArray =
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(binRangeModelList).toByteArray()
                it.write(strToBytes)
            }
            return "json created"
        } catch (ex: Exception) {
            throw CardIdentificationException(ex.message!!)
        }
    }

    private fun mergeExtraDigit(text: String) =
        text.plus(text.takeLast(4))

}