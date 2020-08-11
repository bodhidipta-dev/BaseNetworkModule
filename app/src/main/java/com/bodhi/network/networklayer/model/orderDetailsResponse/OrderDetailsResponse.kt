package com.bodhi.network.networklayer.model.orderDetailsResponse

data class OrderDetailsResponse(
    val environmentAccessLevel: String,
    val error: Error,
    val isEnvironmentDown: Boolean,
    val newPendingOrder: Boolean = false,
    val orderList: List<Order>,
    val status: Int,
    val nextPage: Int?,
    val currentPage: Int,
    val totalItems: Int,
    val perPage: Int
)