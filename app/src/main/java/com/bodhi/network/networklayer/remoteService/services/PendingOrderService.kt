package com.bodhi.network.networklayer.remoteService.services

import com.bodhi.network.networklayer.model.orderDetailsResponse.OrderDetailsResponse
import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.proxy.ProxyTask
import com.bodhi.network.networklayer.proxy.ServiceCallType
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import kotlinx.coroutines.Deferred
import java.lang.reflect.Type

class PendingOrderService<T>(
    val network: NetworkEndpoint,
    val map: Map<*, *>?
) : ProxyTask<T>() {

    override fun serviceCallType(): ServiceCallType = ServiceCallType.NETWORK
    override fun conversionType(): Type = OrderDetailsResponse::class.java
    override fun getServiceCallAsync(): Deferred<T> {
        val data = (map as Map<String, String>)[PROXY_DATA]
        return network.getPendingOrderAsync(data ?: "") as Deferred<T>
    }
}