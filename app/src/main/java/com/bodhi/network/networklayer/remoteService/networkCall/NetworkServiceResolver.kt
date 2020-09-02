package com.bodhi.network.networklayer.remoteService.networkCall

import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.local.PersistenceDao
import com.bodhi.network.networklayer.proxy.Task
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import javax.inject.Inject


class NetworkServiceResolver @Inject constructor(
    private val serviceCall: ServiceCall,
    private val network: NetworkEndpoint
) : RemoteCall {
    /* Mapping for all service call */
    override fun <T : Any> getProxyTask(params: Map<*, *>?, identifier: String): Task<T> {
       /* Here we will pass the necessary arguments to call the service class required */
        return ServiceIdentifier.valueOf(identifier).getServiceProxyTask(
            params = params,
            networkEndpoint = network,
            serviceCall = serviceCall
        )
    }

    /*return database for save information */
    override fun getPersistenceDAO(): PersistenceDao? {
        return serviceCall.getPersistenceDao()
    }
}