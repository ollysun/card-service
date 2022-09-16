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
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Service
class BinRangeService(val binRangeConfiguration: BinRangeConfiguration) {

    private val logger = KotlinLogging.logger {}

    @Value("\${bin.locationBinRangeFile}")
    lateinit var binRangeLocation: String

    @Value("\${bin.elavon-file}")
    lateinit var elavonLocation: String

    fun uploadBinRangesFile(file: MultipartFile): String {
        if (file.isEmpty)
            throw BadRequestException("Empty file")

        try {
            BufferedReader(InputStreamReader(file.inputStream)).use {
                val csvToBean = createBinRangeToBean(it)
                processAndValidateBinRange(csvToBean)

                return "json created"
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

    private fun processAndValidateBinRange(csvBean: CsvToBean<BinRangeUploadModel>) {
        val binRangeUploadModelList: List<BinRangeUploadModel> = csvBean.toList()

        // filter for only non-prepaid accounting funding source
        fun nonPrepaidPredicate(d: BinRangeUploadModel) =
            run { binRangeConfiguration.getBinValidation(Clauses.PREPAID) != d.accountFundingSource } // filter for only non-prepaid accounting funding source

        // check for countryCode
        fun countryPredicate(d: BinRangeUploadModel) =
            run { binRangeConfiguration.getBinValidation(Clauses.COUNTRY_CODE) == d.countryCode }

        // check for currencyCode
        fun currencyCodePredicate(d: BinRangeUploadModel) =
            run { binRangeConfiguration.getBinValidation(Clauses.CURRENCY_CODE) == d.currencyCode }

        // filter for only non-credit
        fun nonCreditPredicate(d: BinRangeUploadModel) =
            run { binRangeConfiguration.getBinValidation(Clauses.CREDIT) != d.creditOrDebit }

        val filterList = binRangeUploadModelList.asSequence()
            .filter {
                nonCreditPredicate(it) && nonPrepaidPredicate(it) && countryPredicate(it)
                        && currencyCodePredicate(it) }.toList()

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

         processAndReturnJson(binList)

    }

   fun processAndReturnJson(binRangeModelList: List<BinRangeModel>) {
        try {
            val mapper = jacksonObjectMapper()
            mapper.findAndRegisterModules()
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // to serialize date

            val file = File(binRangeLocation)
                if(file.exists()){
                    logger.info { "binrange file already exist" }
                    logger.info { "path ${file.absolutePath}" }
                }else {
                    Files.createDirectories(Paths.get(binRangeLocation).parent)
                    logger.info { "path ${file.absolutePath}" }
                    FileOutputStream(file.absolutePath).use {
                        val strToBytes: ByteArray =
                            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(binRangeModelList).toByteArray()
                        it.write(strToBytes)
                }
            }
        } catch (ex: Exception) {
            logger.info { ex.localizedMessage }
            throw CardIdentificationException(ex.message!!)
        }
    }

    private fun mergeExtraDigit(text: String) =
        text.plus(text.takeLast(FOUR_DIGIT))

    // generate the json file on start up
    @PostConstruct
    private fun processJsonFileOnServerStartUp() {
        val cpr = ClassPathResource(elavonLocation)
        try {
            BufferedReader(InputStreamReader(cpr.inputStream)).use {
                val csvToBean = createBinRangeToBean(it)
                return processAndValidateBinRange(csvToBean)
            }
        } catch (ex: Exception) {
            logger.error { ex.localizedMessage }
            throw CardIdentificationException(ex.stackTraceToString())
        }
    }

}