package com.vayapay.cardidentification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CardIdentificationApplication{}

fun main(args: Array<String>) {
	runApplication<CardIdentificationApplication>(*args)
}
