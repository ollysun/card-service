package com.vayapay.cardidentification.model


data class BinRangeModel(
    var acccountRangeLow: String?= null,
    var acccountRangeHigh: String?= null,
    var countryCode: String?= null,
    var currencyCode: String?= null,
    var issueNumberLength: Char?= null,
    var cardType: String?= null,
    var creditOrDebit: Char?= null,
    var cardScheme: String?= null,
    var productId: String?= null,
    var subProductId: String?= null,
    var dccEligibleFlag: String?= null,
    var comboCardFlag: Char?= null,
    var accountFundingSource: String?= null,
    var accountLevelProcessing: String?= null,
    var durbinFlag: String?= null,
    var regulatedOrNonRegulated: String?= null,
    var consumerOrCommercial: String?= null,

)
