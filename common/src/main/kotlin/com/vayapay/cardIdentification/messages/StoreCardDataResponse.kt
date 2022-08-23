package com.vayapay.cardIdentification.messages

import com.vayapay.cardIdentification.model.CardIdResponse
import kotlinx.serialization.Serializable

@Serializable
data class StoreCardDataResponse(
    val cardId: CardIdResponse?,
    override val errorCode: String = "",
    override val errorMessage: String? = null
) : Response() {

    init {
        require(
            ((cardId == null) && (errorCode != ""))
                    || ((cardId != null) && (errorCode == ""))
        ) { "Response can't have token when error has occurred" }
    }
}