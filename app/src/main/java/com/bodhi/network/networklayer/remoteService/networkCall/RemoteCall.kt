package com.bodhi.network.networklayer.remoteService.networkCall

import com.bodhi.network.networklayer.proxy.Task
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier

interface RemoteCall {
    fun <T> getProxyTask(
        params: Map<*, *>?,
        identifier: ServiceIdentifier
    ): Task<T>
}