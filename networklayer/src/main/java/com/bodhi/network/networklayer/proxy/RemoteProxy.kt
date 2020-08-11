package com.bodhi.network.networklayer.proxy

import com.bodhi.network.networklayer.ServiceCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
interface RemoteProxy<T> {
    fun preferenceUniqueId(): String {
        return ""
    }
    fun provideTaskAsync(identifier: String, serviceCall: ServiceCall): Task<T>
}

interface Task<T> {
    fun executeFlow(coroutineScope: CoroutineScope): Flow<T>
}