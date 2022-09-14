package com.vayapay.cardidentification.core.controller;

import com.ninjasquad.springmockk.MockkBean
import com.vayapay.cardidentification.controller.BinRangesImportController
import com.vayapay.cardidentification.core.BinRangeService
import io.mockk.mockk
import io.rsocket.core.RSocketServer
import io.rsocket.frame.decoder.PayloadDecoder
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.transport.netty.server.TcpServerTransport
import org.junit.Before
import org.junit.BeforeClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.rsocket.server.RSocketServerException
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.messaging.rsocket.connectTcpAndAwait
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.io.InputStream
import javax.xml.bind.JAXBElement.GlobalScope

@AutoConfigureMockMvc
@SpringBootTest
internal class BinRangeImportControllerTest(@Autowired var mockMvc:MockMvc){

    private var `is`: InputStream? = null

//    @Mock
//    private val binRangeService: BinRangeService? = null

    @LocalRSocketServerPort val port = 4141

    private val mockBinRangeService = mockk<BinRangeService>()
    private val binController = BinRangesImportController(mockBinRangeService)


    var rSocketRequester: RSocketRequester? = null

    @MockkBean
    var requestBuilder = RSocketRequester.builder()

    @BeforeEach
    fun init() {
        mockMvc = MockMvcBuilders.standaloneSetup(binController).build()
        `is` = ClassPathResource("elavon.csv").inputStream
        // Create an RSocket server for use in testing
        rSocketRequester = requestBuilder.tcp("localhost", port)
    }


    @Test
    @Throws(Exception::class)
    fun testUploadFile() {
        val mockMultipartFile = MockMultipartFile("file", "elavon.csv", "multipart/form-data", `is`!!)
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
