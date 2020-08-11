package com.bodhi.network.networklayer.remoteService.networkCall

import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.local.PersistenceDao
import com.bodhi.network.networklayer.proxy.Task
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import com.bodhi.network.networklayer.remoteService.services.AuthenticationService
import com.bodhi.network.networklayer.remoteService.services.PendingOrderService
import javax.inject.Inject


class NetworkImpl @Inject constructor(
    private val serviceCall: ServiceCall,
    private val network: NetworkEndpoint
) : RemoteCall {
/* Mapping for all service call */
    override fun <T : Any> getProxyTask(params: Map<*, *>?, identifier: String): Task<T> {
        return when (identifier) {
            ServiceIdentifier.AUTHENTICATION.name -> AuthenticationService<T>(network, params)
            ServiceIdentifier.PENDING_ORDER.name -> PendingOrderService<T>(network, params)
            else -> throw ClassNotFoundException("No Such service found")
        }.provideTaskAsync(identifier = identifier, serviceCall = serviceCall)

    }

    /*return database for save information */
    override fun getPersistenceDAO(): PersistenceDao? {
        return serviceCall.getPersistenceDao()
    }
}