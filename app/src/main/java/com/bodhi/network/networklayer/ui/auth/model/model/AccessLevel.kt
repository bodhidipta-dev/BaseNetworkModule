package com.bodhi.network.networklayer.ui.auth.model.model

data class AccessLevel(
    val accessExpiry: String,
    val level: String,
    val shouldPersistAccess: Boolean
)