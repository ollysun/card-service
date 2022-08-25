package com.vayapay.cardidentification.core

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vayapay.cardidentification.dto.BinRangeDTO
import com.vayapay.cardidentification.exception.BadRequestException
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.BinRangeModel
import com.vayapay.cardidentification.repo.BinRange
import com.vayapay.cardidentification.repo.BinRangeRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.function.Predicate
import java.util.stream.Collectors

@Service
class CsvService(
    val binRepository: BinRangeRepository) {

    suspend fun uploadCsvFile(file: MultipartFile): List<BinRange> {
        if (file.isEmpty)
            throw BadRequestException("Empty file")

        var fileReader: BufferedReader? = null

        try {
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val csvToBean = createCSVToBean(fileReader)

            return processAndValidateCSV(csvToBean)
        } catch (ex: Exception) {
            throw CardIdentificationException("Error during csv import")
        } finally {
            closeFileReader(fileReader)
        }
    }

    private fun createCSVToBean(fileReader: BufferedReader?): CsvToBean<BinRangeModel> =
        CsvToBeanBuilder<BinRangeModel>(fileReader)
            .withType(BinRangeModel::class.java)
            .withSkipLines(1)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private suspend fun processAndValidateCSV(csvBean : CsvToBean<BinRangeModel>) : ArrayList<BinRange> {
        val binRangeModelList : List<BinRangeModel> = csvBean.toList();
        // filter for only non-prepaid accounting funding source
        val nonPrepaidPredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> 'P' != d.accountFundingSource } // filter for only non-prepaid accounting funding source
        // check for countrycode
        val countryPredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> "578".equals(d.countryCode) }
        // check for currencycode
        val currencyCodePredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> "578".equals(d.currencyCode) }
        // filter for only non-credit
        val nonCreditPredicate: Predicate<BinRangeModel> = Predicate<BinRangeModel> { d -> 'C'!= d.creditOrDebit }

        val filterList = binRangeModelList.stream()
            .filter(nonCreditPredicate.and(nonPrepaidPredicate).and(countryPredicate).and(currencyCodePredicate))
            .collect(Collectors.toList())
        val binList =  arrayListOf<BinRange>()
        for(bin in filterList){
            val binRange = BinRange(
                null,
                bin.acccountRangeLow.toString(),
                bin.acccountRangeHigh.toString(),
                checkCardScheme(bin.cardScheme.toString()))
            binList.add(binRepository.save(binRange))
        }
        return binList;

    }

    private fun closeFileReader(fileReader: BufferedReader?) {
        try {
            fileReader!!.close()
        } catch (ex: IOException) {
            throw CardIdentificationException("Error during csv import")
        }
    }

    private  fun checkCardScheme( schem: String) : String{
        val schemeMap = mapOf("4B" to "Banco Santander",
            "AX" to "American Express (Amex)",
            "DC" to "Diners Club",
            "DI" to "Discover",
            "GC" to "Gift Card",
            "JC" to "Japan Credit Bureau (JCB)",
            "MC" to "MasterCard",
            "SH" to "Shell",
            "UP" to "Union Pay",
            "UD" to "Union Pay",
            "VI" to "Visa")
        return schemeMap.getOrDefault(schem, "")
    }

}