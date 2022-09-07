package com.vayapay.cardidentification.core

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration
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
