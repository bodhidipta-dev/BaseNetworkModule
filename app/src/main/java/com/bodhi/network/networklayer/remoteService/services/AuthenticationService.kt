package com.bodhi.network.networklayer.remoteService.services

import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.proxy.ProxyTask
import com.bodhi.network.networklayer.proxy.ServiceCallType
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.ui.auth.model.AuthRequestModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.lang.reflect.Type

class AuthenticationService<T>(
    val network: NetworkEndpoint,
    val map: Map<*, *>?
) : ProxyTask<T>() {
    override fun getServiceCall(): Observable<T> {
        val data = (map as Map<String, AuthRequestModel>)[PROXY_DATA]
        return network.authenticateRestaurant(data) as Observable<T>
    }

    override fun serviceCallType(): ServiceCallType = ServiceCallType.NETWORK

    override fun conversionType(): Type = ResponseBody::class.java
}