package com.vayapay.cardIdentification.core.controller;

import com.vayapay.cardIdentification.controller.BinRangesImportController
import com.vayapay.cardIdentification.core.BinRangeService
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.io.InputStream


@AutoConfigureMockMvc
@SpringBootTest
class BinRangeImportControllerTest{

    private final var mockMvc: MockMvc

    private var `is`: InputStream? = null
    private var mockBinRangeService : BinRangeService = mockk()
    private val binController = BinRangesImportController(mockBinRangeService)

    init{
        mockMvc = MockMvcBuilders.standaloneSetup(binController).build()
        `is` = binController.javaClass.classLoader.getResourceAsStream("elavon.csv");
    }

    @Test
    fun testUploadFile() {

        val mockMultipartFile = MockMultipartFile("file", "elavon.csv", "multipart/form-data", `is`!!)
        coEvery {
            mockBinRangeService.uploadBinRangesFile(mockMultipartFile)
        } returns "json created"
        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/bin-range/bin").file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200)).andReturn()
        Assertions.assertEquals(200, result.response.status)
        Assertions.assertNotNull(result.response.contentAsString)
        Assertions.assertEquals("json created", result.response.contentAsString)
    }

}
