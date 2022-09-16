package com.vayapay.cardIdentification.core

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "card")
class BinRangeConfiguration {
    val cardScheme = HashMap<String, String>()
    val flytoget = HashMap<String, String>()
    fun getCardScheme(clauses: String): String? {
        return cardScheme[clauses];
    }

    fun getBinValidation(binValidation: Clauses): String? {
        return flytoget[binValidation.name]
    }

}
