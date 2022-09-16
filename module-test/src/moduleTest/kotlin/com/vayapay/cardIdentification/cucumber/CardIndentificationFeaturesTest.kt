package com.vayapay.cardIdentification.cucumber

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty", "html:build/test-results/moduleTest/cardDataService-features.html", "junit:build/test-results/moduleTest/cardDataService-features.xml"],
    features = ["src/moduleTest/features"],
    glue = ["com.vayapay.cardidentification.cucumber.config", "com.vayapay.cardientification.cucumber.steps"]
)
class CardIndentificationFeaturesTest {
}