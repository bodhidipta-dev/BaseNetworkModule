package com.bodhi.network.networklayer.proxy

import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.local.NetworkCallCache
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import timber.log.Timber
import java.lang.reflect.Type

class ExecuteServiceTask<T>(
    private val identifier: String,
    private val uniqueIdentity: String,
    private val serviceCallType: ServiceCallType,
    private val requestCall: Deferred<T>,
    private val serviceCall: ServiceCall,
    private val returnType: Type
) :
    Task<T> {
    override fun executeFlow(coroutineScope: CoroutineScope): Flow<T> {
        return when (serviceCallType) {
            ServiceCallType.PERSISTENCE -> {
                flow {
                    // create a new flow for emit
                    val cachedData = serviceCall.getPersistenceDao()?.getResponseByIdentifierAsync(identifier)
                    // checked data check and emit
                    cachedData?.let { callCache ->
                        this.emit(Gson().fromJson<T>(callCache.responseSaved, returnType))
                    }
                    // run network call
                    val latestResponse = requestCall.runCatching {
                        this.await()
                    }.onFailure {
                        // on fail to get remote
                        throw RequestError("Couldn't reach server")
                    }.onSuccess {
                        if (cachedData==null)
                            this.emit(it)
                        serviceCall.getPersistenceDao()?.updateResponse(
                            NetworkCallCache(
                                identifier + uniqueIdentity,
                                Gson().toJson(it),
                                System.currentTimeMillis().toString()
                            )
                        )
                    }
                    latestResponse.getOrNull()
                }
            }
            ServiceCallType.NETWORK -> {
                flow {
                    emit(requestCall.await())
                }
            }
            ServiceCallType.CACHE -> {
                flow {
                    serviceCall.getSecurePreferences()
                        .getString(identifier, null)?.let {
                            emit(Gson().fromJson(it, returnType) as T)
                        }
                }
            }
        }.apply {
            launchIn(coroutineScope)
            Timber.i("Executing on the context: ->${coroutineScope.coroutineContext}")
        }
    }
}