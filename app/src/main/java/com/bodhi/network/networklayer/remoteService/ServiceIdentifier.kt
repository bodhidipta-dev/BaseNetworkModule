package com.bodhi.network.networklayer.remoteService

import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.proxy.ProxyTask
import com.bodhi.network.networklayer.proxy.Task
import com.bodhi.network.networklayer.remoteService.services.AuthenticationService
import com.bodhi.network.networklayer.remoteService.services.PendingOrderService
import kotlin.reflect.KClass

/**
 * This class plays a role of keeping services together and dispatch service on required
 */
enum class ServiceIdentifier(private val mappedClass: KClass<*>) {
    AUTHENTICATION(AuthenticationService::class),
    PENDING_ORDER(PendingOrderService::class);

    fun <T> getServiceProxyTask(
        params: Map<*, *>?,
        serviceCall: ServiceCall,
        networkEndpoint: NetworkEndpoint
    ): Task<T> {
        return if (this.mappedClass.supertypes.any {
                it.classifier == ProxyTask::class
            }) ((this.mappedClass.constructors as List).first()
            .call(networkEndpoint, params) as ProxyTask<T>)
            .provideTaskAsync(
                serviceCall = serviceCall,
                identifier = this.name
            )
        else throw ClassNotFoundException(
            "Make Sure you have entered" +
                    " all classes for mappedClass that extends ProxyTask<T>"
        )
    }
}