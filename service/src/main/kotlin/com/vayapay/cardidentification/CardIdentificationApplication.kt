package com.vayapay.cardidentification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = ["com.vayapay.cardidentification", "com.vayapay.carddata"])
class CardIdentificationApplication{}

fun main(args: Array<String>) {
	runApplication<CardIdentificationApplication>(*args)
}
