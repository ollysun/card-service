package com.vayapay.cardidentification.model

import com.opencsv.bean.CsvBindByPosition


data class BinRangeModel(
    @CsvBindByPosition(position = 0)
    var acccountRangeLow: String?= null,
    @CsvBindByPosition(position = 1)
    var acccountRangeHigh: String?= null,
    @CsvBindByPosition(position = 2)
    var countryCode: String?= null,
    @CsvBindByPosition(position = 3)
    var currencyCode: String?= null,
    @CsvBindByPosition(position = 4)
    var issueNumberLength: Char?= null,
    @CsvBindByPosition(position = 5)
    var cardType: String?= null,
    @CsvBindByPosition(position = 6)
    var creditOrDebit: Char?= null,
    @CsvBindByPosition(position = 7)
    var cardScheme: String?= null,
    @CsvBindByPosition(position = 8)
    var productId: String?= null,
    @CsvBindByPosition(position = 9)
    var subProductId: String?= null,
    @CsvBindByPosition(position = 10)
    var dccEligibleFlag: String?= null,
    @CsvBindByPosition(position = 11)
    var comboCardFlag: Char?= null,
    @CsvBindByPosition(position = 12)
    var accountFundingSource: String?= null,
    @CsvBindByPosition(position = 13)
    var accountLevelProcessing: String?= null,
    @CsvBindByPosition(position = 14)
    var durbinFlag: String?= null,
    @CsvBindByPosition(position = 15)
    var regulatedOrNonRegulated: String?= null,
    @CsvBindByPosition(position = 16)
    var consumerOrCommercial: String?= null,

)
