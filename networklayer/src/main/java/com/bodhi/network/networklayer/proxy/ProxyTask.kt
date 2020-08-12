package com.bodhi.network.networklayer.proxy

import com.bodhi.network.networklayer.ServiceCall
import kotlinx.coroutines.Deferred
import java.lang.reflect.Type

const val PROXY_DATA = "Data"

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */

abstract class ProxyTask<T> :
    RemoteProxy<T>  {
    abstract fun serviceCallType(): ServiceCallType
    abstract fun conversionType(): Type
    override fun preferenceUniqueId(): String = ""
    override fun provideTaskAsync(
        identifier: String,
        serviceCall: ServiceCall
    ): Task<T> {
        return ExecuteServiceTask(
            identifier,
            preferenceUniqueId(),
            serviceCallType(),
            getServiceCallAsync(),
            serviceCall,
            conversionType()
        )
    }
    abstract fun getServiceCallAsync(): Deferred<T>
}