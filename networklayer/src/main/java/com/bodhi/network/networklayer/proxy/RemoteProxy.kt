package com.bodhi.network.networklayer.proxy

import com.bodhi.network.networklayer.ServiceCall
import io.reactivex.disposables.Disposable

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
interface RemoteProxy<T> {
    fun preferenceUniqueId(): String
    fun provideTask(identifier: String, serviceCall: ServiceCall): Task<T>
}

interface Task<T> {
    fun execute(
        onsuccess: (T) -> Unit,
        onerror: (ex: Throwable) -> Unit
    ): Disposable
}