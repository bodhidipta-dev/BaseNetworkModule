package com.bodhi.network.networklayer

import com.bodhi.network.networklayer.proxy.Task

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
interface RemoteCall {
    fun <T> getProxyTask(
        params: Map<*, *>?,
        identifier: String
    ): Task<T>
}