package com.vayapay.cardidentification.core.controller

import com.ninjasquad.springmockk.MockkBean
import com.vayapay.cardidentification.controller.BinRangesImportController
import com.vayapay.cardidentification.core.BinRangeService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@WebMvcTest(BinRangesImportController::class)
class BinRangeControllerTest {



    @MockkBean
    lateinit var binRangeService: BinRangeService

    @Test
    fun `test Upload Bin Ranges`() {
        val fileResource: Resource = ClassPathResource(
            "ELAVON_ACCTRNG_20220729.csv"
        )
        assertNotNull(fileResource)
        val binFile = MockMultipartFile(
            "attachments", fileResource.filename,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            fileResource.inputStream
        )
        val response : String = binRangeService.uploadBinRangesFile(binFile)
        assertNotNull(binFile)
        assertEquals(response,"json created")
    }


}