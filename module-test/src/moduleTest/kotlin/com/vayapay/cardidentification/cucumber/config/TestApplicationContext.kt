package com.vayapay.cardidentification.cucumber.config

import com.vayapay.cardidentification.core.CardIdentificationService
import com.vayapay.cucumber.CucumberConfiguration
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import com.vayapay.mock.rsocket.server.RSocketMockConfiguration


@CucumberContextConfiguration
@ContextConfiguration(
    classes = [CucumberConfiguration::class, RSocketMockConfiguration::class]
)
@SpringBootTest(
    classes = [CardIdentificationService::class])
class TestApplicationContext {
}