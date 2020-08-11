package com.bodhi.network.networklayer.model.orderDetailsResponse

import com.bodhi.network.networklayer.model.CustomerInfo

data class Order(
    val orderId: String,
    val orderItems: List<OrderItem>,
    val totalBillAmount: String,
    val paymentID: String,
    val customerDetails: CustomerInfo,
    val orderDate: String,
    val orderTime: String
)