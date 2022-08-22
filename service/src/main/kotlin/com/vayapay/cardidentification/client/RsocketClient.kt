package com.vayapay.cardidentification.core

import com.vayapay.cardidentification.messages.StoreCardDataResponse
import com.vayapay.cardidentification.model.StoreCardDataRequest
import io.rsocket.transport.netty.client.TcpClientTransport
import mu.KotlinLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.stereotype.Component
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils
import reactor.core.publisher.Mono
import reactor.netty.tcp.TcpClient
import reactor.util.retry.Retry
import java.time.Duration

const val STORE_CARD_DATA = "cardStorage/storeCardData"


@Configuration
class CardDataStorageRSocketTransportConfiguration(
    @Value("\${cardDataService.host:localhost}") private val cardDataHost: String,
    @Value("\${cardDataService.port:4040}") private val cardDataPort: Int
) {

    @Bean("rSocketCardDataClientTransport")
    fun rSocketCardDataClientTransport() =
        TcpClientTransport.create(
            TcpClient.create()
                .host(cardDataHost)
                .port(cardDataPort)
        )
}

@Component
class RSocketCardDataClient(
    @Autowired private val requestBuilder: RSocketRequester.Builder,
    @Autowired(required = false) @Qualifier("acquireGatewayCustomRSocketStrategies") customRSocketStrategies: ObjectProvider<RSocketStrategiesCustomizer>? = null,
    @Autowired @Qualifier("rSocketCardDataClientTransport") transport: TcpClientTransport
) {

    private val logger = KotlinLogging.logger {}

    private val requester: RSocketRequester

    init {
        val strategies = RSocketStrategies.builder()
            .decoder(KotlinSerializationJsonDecoder())
            .encoder(KotlinSerializationJsonEncoder())

        // Additional strategies
        customRSocketStrategies?.forEach {
            it.customize(strategies)
        }
        requester = requestBuilder
            .dataMimeType(MimeType.valueOf(MimeTypeUtils.APPLICATION_JSON_VALUE))
            .rsocketStrategies(strategies.build())
            .rsocketConnector { it.reconnect(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(800))) }
            .transport(transport)

    }



     fun storeCardData(storeCardDataRequest: StoreCardDataRequest): Mono<StoreCardDataResponse> {
        return requester
                .route(STORE_CARD_DATA)
                .data(
                    StoreCardDataRequest(
                        ptoId = storeCardDataRequest.ptoId,
                        cardData = storeCardDataRequest.cardData
                    )
                ).retrieveMono(StoreCardDataResponse::class.java)

    }




}

