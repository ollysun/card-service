package com.vayapay.cardIdentification.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.AddCardForm
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@ContextConfiguration()
@WebMvcTest(CardIdentificationController::class)
@WithMockUser
internal class CardControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var cardService: CardIdentificationService

    @BeforeEach
    fun setup() {
        runBlocking {
            mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
        }
    }

    @Test
    fun `should render card registration form`() {
        runBlocking {
            mockMvc.get("/card")
                .andExpect {
                    status { isOk() }
                    content {
                        string("")
                    }
                }
        }
    }

    @Test
    fun `should post cardFormData`() {
        runBlocking {
            val addCardForm = AddCardForm("378282246310005", "09", "24")
            mockMvc.post("/card") {
                content = jacksonObjectMapper().writeValueAsString(addCardForm)
                with(SecurityMockMvcRequestPostProcessors.csrf())
            }.andExpect {
                status { isOk() }
                content {
                    string("")
                }
            }
        }
    }
}