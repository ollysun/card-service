package com.vayapay.cardidentification.model

import com.opencsv.bean.CsvBindByPosition
import java.time.Instant

data class BinRangeUploadModel(
    @CsvBindByPosition(position = 0)
    val accountRangeLow: String?= null,
    @CsvBindByPosition(position = 1)
    val accountRangeHigh: String?= null,
    @CsvBindByPosition(position = 2)
    val countryCode: String?= null,
    @CsvBindByPosition(position = 3)
    val currencyCode: String?= null,
    @CsvBindByPosition(position = 4)
    val issueNumberLength: Char?= null,
    @CsvBindByPosition(position = 5)
    val cardType: String?= null,
    @CsvBindByPosition(position = 6)
    val creditOrDebit: String?= null,
    @CsvBindByPosition(position = 7)
    val cardScheme: String?= null,
    @CsvBindByPosition(position = 8)
    val productId: String?= null,
    @CsvBindByPosition(position = 9)
    val subProductId: String?= null,
    @CsvBindByPosition(position = 10)
    val dccEligibleFlag: Char?= null,
    @CsvBindByPosition(position = 11)
    val comboCardFlag: Char?= null,
    @CsvBindByPosition(position = 12)
    val accountFundingSource: String?= null,
    @CsvBindByPosition(position = 13)
    val accountLevelProcessing: String?= null,
    @CsvBindByPosition(position = 14)
    val durbinFlag: Char?= null,
    @CsvBindByPosition(position = 15)
    val regulatedOrNonRegulated: String?= null,
    @CsvBindByPosition(position = 16)
    val consumerOrCommercial: String?= null

)


data class BinRange(
    val id: Int,
    val binRangeFrom: String,
    val binRangeTo: String,
    val brand: String,
    val createTime: Instant = Instant.now()
)