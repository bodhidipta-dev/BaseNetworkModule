package com.bodhi.network.networklayer.model

data class CustomerInfo(
    val address: String,
    val customerId: String,
    val customerName: String,
    val email: String,
    val primaryContact: String,
    val secondaryContact: String
)