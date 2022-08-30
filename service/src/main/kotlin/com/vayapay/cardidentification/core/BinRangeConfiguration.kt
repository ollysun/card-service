package com.vayapay.cardidentification.core

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "card")
data class BinRangeConfiguration(
    val cardScheme: Map<String, String>,
    val flytoget: Map<String, String>
){

    fun getCardScheme(clauses: String): String? {
        return cardScheme[clauses];
    }

    fun getBinValidation(binValidation : CLAUSES) : String? {
        return flytoget[binValidation.name]
    }

}
