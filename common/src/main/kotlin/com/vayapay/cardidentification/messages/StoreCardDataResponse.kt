package com.vayapay.cardidentification.messages

import com.vayapay.cardidentification.model.CardId
import kotlinx.serialization.Serializable

@Serializable
data class StoreCardDataResponse(
    val cardId : CardId?,
    override val errorCode: String = "",
    override val errorMessage:String?=null) : Response() {

    init {
        require(((cardId == null) && (errorCode != ""))
                || ((cardId != null) && (errorCode == ""))) {"Response can't have token when error has occurred"}
    }
}