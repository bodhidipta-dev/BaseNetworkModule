package com.bodhi.network.networklayer.remoteService.services

import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.proxy.ProxyTask
import com.bodhi.network.networklayer.proxy.ServiceCallType
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.ui.auth.model.AuthRequestModel
import com.bodhi.network.networklayer.ui.auth.model.model.AuthResponse
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import java.lang.reflect.Type

class AuthenticationService<T>(
    val network: NetworkEndpoint,
    val map: Map<*, *>?
) : ProxyTask<T>() {
    /* Normal network call */
    override fun serviceCallType(): ServiceCallType = ServiceCallType.NETWORK

    override fun conversionType(): Type = AuthResponse::class.java
    override fun getServiceCallAsync(): Deferred<T> {
        val data = (map as Map<String, AuthRequestModel>)[PROXY_DATA]
        return network.authenticateAsync(data) as Deferred<T>
    }
}