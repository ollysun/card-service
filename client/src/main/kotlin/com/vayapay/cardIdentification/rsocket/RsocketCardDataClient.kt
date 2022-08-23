package com.vayapay.cardIdentification.rsocket

import com.vayapay.cardIdentification.CardDataStorageClient
import com.vayapay.cardIdentification.messages.Response
import com.vayapay.cardIdentification.messages.STORE_CARD_DATA
import com.vayapay.cardIdentification.messages.StoreCardDataResponse
import com.vayapay.cardIdentification.model.CardData
import com.vayapay.cardIdentification.model.CardIdResponse
import com.vayapay.cardIdentification.model.StoreCardDataRequest
import io.rsocket.transport.netty.client.TcpClientTransport
import kotlinx.coroutines.reactive.awaitFirst
import mu.KotlinLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
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

@Configuration
class CardDataStorageRSocketConfiguration {
    @Bean("cardDataStorageCustomRSocketStrategies")
    fun customRSocketStrategies(): RSocketStrategiesCustomizer? {
        return RSocketStrategiesCustomizer {
            // Add strategies which are required for card data storage client
        }
    }

    @Bean("cardDataStorageRSocketRetryConfig")
    @ConditionalOnMissingBean(name = ["cardDataStorageRSocketRetryConfig"])
    fun cardDataStorageRSocketRetryConfig(): Retry = Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(800))
}

@Configuration
class CardDataStorageRSocketTransportConfiguration(
    @Value("\${cardDataService.host:localhost}") private val cardDataHost: String,
    @Value("\${cardDataService.port:4040}") private val cardDataPort: Int
) {
    @Bean("rSocketCardDataClientTransport")
    @Profile("sslEnabled")
    fun rSocketCardDataClientSSLTransport() =
        TcpClientTransport.create(
            TcpClient.create()
                .secure()
                .host(cardDataHost)
                .port(cardDataPort)
        )

    @Bean("rSocketCardDataClientTransport")
    @Profile("!sslEnabled")
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
    @Autowired @Qualifier("cardDataStorageRSocketRetryConfig") retry: Retry,
    @Autowired @Qualifier("rSocketCardDataClientTransport") transport: TcpClientTransport
) : CardDataStorageClient {

    private val logger = KotlinLogging.logger {}

    private val requester: RSocketRequester

    init {
        // mandatory rsocket strategies
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
            .rsocketConnector { it.reconnect(retry) }
            .transport(transport)
    }


    override suspend fun storeCardData(ptoId: String, cardData: CardData): CardIdResponse? {

        return handleResponse<StoreCardDataResponse>(
            requester
                .route(STORE_CARD_DATA)
                .data(
                    StoreCardDataRequest(
                        ptoId = ptoId,
                        cardData = cardData
                    )
                )
                .retrieveMono(StoreCardDataResponse::class.java)
        )?.cardId

    }

    private suspend fun <I : Response> handleResponse(response: Mono<I>) =
        response
            .doOnSuccess {
                // Check whether request was handled correctly
                if (it.errorCode != "") {
                    logger.warn("Error[${it.errorCode}:${it.errorMessage}] occurred while tokenizing card data")
                }
            }
            .doOnError {
                // Log exception
                logger.error("Error occurred while tokenizing card data: ${it.message}", it)
            }.awaitFirst()

}