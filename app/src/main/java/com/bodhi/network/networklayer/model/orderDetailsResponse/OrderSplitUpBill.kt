package com.bodhi.network.networklayer.model.orderDetailsResponse

data class OrderSplitUpBill(
    val discount: String,
    val discountAmount: String,
    val displayAmount: String,
    val factor_price: String,
    val gst: String,
    val price: String
)