package com.bodhi.network.networklayer.ui.auth.model.model

data class Response(
    val accessLevel: AccessLevel,
    val accessToken: String,
    val grantedAccess: Boolean
)