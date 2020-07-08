package com.bodhi.network.networklayer.remoteService.networkCall

import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.proxy.Task
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.remoteService.services.AuthenticationService
import javax.inject.Inject


class NetworkImpl @Inject constructor(private val serviceCall: ServiceCall) : RemoteCall {
    private val network: NetworkEndpoint = serviceCall.serviceEndpoints(NetworkEndpoint::class.java)

    override fun <T> getProxyTask(
        params: Map<*, *>?,
        identifier: ServiceIdentifier
    ): Task<T> {
        return when (identifier) {
            ServiceIdentifier.AUTHENTICATION -> AuthenticationService<T>(network, params)
        }.provideTask(identifier = identifier.name,serviceCall = serviceCall)
    }
}