package com.net.pvr1.ui.giftCard.response
import java.io.Serializable

data class GiftCardListResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable {
    data class Output(
        val bannerUrl: String,
        val c: String,
        val deliveryCharge: Int,
        val discount: Any,
        val giftCards: ArrayList<GiftCard>,
        val imageUrl: String,
        val limit: String,
        val promotional: Boolean,
        val timer: Int
    ) : Serializable {
        data class GiftCard(
            val active: Boolean,
            val alias: String,
            val allowedCount: Int,
            val channel: String,
            val display: String,
            val giftValue: Int,
            val imageUrl: String,
            val newImageUrl: String,
            val pkGiftId: Int,
            val productDescription: String,
            val sku: String,
            val skuPrice: Int,
            val c: Int=allowedCount,
            var count: Int =0,
            val skuQuantity: Int,
            val type: String,
            val updateDate: Long,
            ):Serializable
    }
}