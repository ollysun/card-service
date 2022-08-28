package com.vayapay.cardidentification.core

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRangeModel
import com.vayapay.cardidentification.repo.BinRange
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.function.Predicate
import java.util.stream.Collectors

@Service
class BinRangeService(
    val binRangeConfiguration:  BinRangeConfiguration) {

    suspend fun uploadBinRangesFile(file: MultipartFile): List<BinRange> {
        if (file.isEmpty)
            throw CardIdentificationException("Empty file")

        try {
            BufferedReader(InputStreamReader(file.inputStream)).use {
                val csvToBean = createCSVToBean(it)
                return processAndValidateCSV(csvToBean)
            }
        } catch (ex: Exception) {
            throw CardIdentificationException("Error during csv import")
        }
    }

    private fun createCSVToBean(fileReader: BufferedReader?): CsvToBean<BinRangeModel> =
        CsvToBeanBuilder<BinRangeModel>(fileReader)
            .withType(BinRangeModel::class.java)
            .withSkipLines(1) // this is to skip the header
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private suspend fun processAndValidateCSV(csvBean : CsvToBean<BinRangeModel>) : ArrayList<BinRange> {
        val binRangeModelList : List<BinRangeModel> = csvBean.toList()
        // filter for only non-prepaid accounting funding source
        val nonPrepaidPredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.PREPAID) == d.accountFundingSource } // filter for only non-prepaid accounting funding source
        // check for countryCode
        val countryPredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.COUNTRY_CODE) == d.countryCode }
        // check for currencyCode
        val currencyCodePredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.CURRENCY_CODE) == d.currencyCode }
        // filter for only non-credit
        val nonCreditPredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> binRangeConfiguration.getBinValidation(CLAUSES.CREDIT) != d.creditOrDebit }

        val filterList = binRangeModelList.stream()
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
        return binList

    }

    private fun mergeExtraDigit(text: String) =
        text.plus(text.takeLast(4))

}