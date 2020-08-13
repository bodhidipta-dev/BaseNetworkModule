package com.bodhi.network.networklayer.remoteService

import com.bodhi.network.networklayer.model.orderDetailsResponse.OrderDetailsResponse
import com.bodhi.network.networklayer.ui.auth.model.AuthRequestModel
import com.bodhi.network.networklayer.ui.auth.model.model.AuthResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NetworkEndpoint {
    @POST("getAuthenticate")
    fun authenticateAsync(@Body credential: AuthRequestModel?): Deferred<AuthResponse>

    @POST("getPendingOrder")
    fun getPendingOrderAsync(@Header("Authorisation") token: String): Deferred<OrderDetailsResponse>

}