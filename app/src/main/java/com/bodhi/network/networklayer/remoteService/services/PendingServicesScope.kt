package com.bodhi.network.networklayer.remoteService.services

import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.model.orderDetailsResponse.Order
import com.bodhi.network.networklayer.model.orderDetailsResponse.OrderDetailsResponse
import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.proxy.PagingProxyTaskAsync
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class PendingServicesScope constructor(
    private val remoteCall: RemoteCall,
    private val accessToken: String
) :
    PagingProxyTaskAsync<Int, Order, OrderDetailsResponse>() {
/* For pagination enabled calls */
    override fun transFormData(response: OrderDetailsResponse): List<Order> {
        return (response as OrderDetailsResponse).orderList
    }

    override fun getKey(response: LoadParams<Int>, t: OrderDetailsResponse?): Int? {
        return if (response.key == null && t == null) 1 else {
            response.key?.let {
                if (it < t?.totalItems ?: 0)
                    t?.nextPage ?: null
                else
                    null
            } ?: null
        }
    }

    override suspend fun getServiceCall(loadParams: Int?): OrderDetailsResponse {
        return remoteCall.getProxyTask<OrderDetailsResponse>(
            mapOf(PROXY_DATA to accessToken),
            ServiceIdentifier.PENDING_ORDER.name
        ).executeFlow(CoroutineScope(Dispatchers.IO)).single()
    }


}