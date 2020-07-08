package com.bodhi.network.networklayer.ui.auth.model.model

data class AuthResponse(
    val environmentAccessLevel: String,
    val error: Error,
    val isEnvironmentDown: Boolean,
    val response: Response,
    val status: Int
)