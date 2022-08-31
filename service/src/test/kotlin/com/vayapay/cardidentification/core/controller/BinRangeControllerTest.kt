package com.vayapay.cardidentification.core.controller

import com.ninjasquad.springmockk.MockkBean
import com.vayapay.cardidentification.controller.BinRangesImportController
import com.vayapay.cardidentification.core.BinRangeService
import junit.framework.TestCase.assertNotNull
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.io.InputStream


@RunWith(SpringRunner::class)
@WebMvcTest(BinRangesImportController::class)
class BinRangeControllerTest() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var binRangeService: BinRangeService

    @MockBean
    private val binRangesImportController: BinRangesImportController? = null
    @Before
    fun init() {
        mockMvc = MockMvcBuilders.standaloneSetup(binRangesImportController).build()
    }

    @Test
    fun `test Upload Bin Ranges`() {
        val fileResource: Resource = ClassPathResource(
            "ELAVON_ACCTRNG_20220729.csv"
        )
        assertNotNull(fileResource)
        val mockMultipartFile = MockMultipartFile("file", "ELAVON_ACCTRNG_20220729.csv",
            "multipart/form-data",
            fileResource.inputStream)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/bin-range/bin").file(mockMultipartFile).contentType(
                MediaType.MULTIPART_FORM_DATA
            )
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200)).andReturn()
            Assert.assertEquals(200, result.getResponse().getStatus());
    }


}