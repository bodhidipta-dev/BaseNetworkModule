package com.bodhi.network.networklayer.proxy

import androidx.paging.PagingSource
import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.local.NetworkCallCache
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type

const val PROXY_DATA = "Data"

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
abstract class PagingProxyTaskAsync<KeyType : Any, ListType : Any, Response> :
    PagingSource<KeyType, ListType>() {
    override suspend fun load(params: LoadParams<KeyType>): LoadResult<KeyType, ListType> {
        return getKey(response = params)?.let {
            try {
                val responseData = getServiceCall(it)
                LoadResult.Page<KeyType, ListType>(
                    data = transFormData(responseData),
                    nextKey = it,
                    prevKey = null
                )
            } catch (e: IOException) {
                // IOException for network failures.
                return LoadResult.Error(e)
            } catch (e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                return LoadResult.Error(e)
            }
        } ?: LoadResult.Error(Throwable("No More item to Load"))
    }

    abstract fun transFormData(t: Response): List<ListType>
    abstract fun getKey(response: LoadParams<KeyType>, t: Response? = null): KeyType?
    abstract suspend fun getServiceCall(loadParams: KeyType?): Response
}

abstract class ProxyTask<T> :
    RemoteProxy<T> {
    abstract fun serviceCallType(): ServiceCallType
    abstract fun conversionType(): Type
    override fun preferenceUniqueId(): String = ""
    override fun provideTaskAsync(
        identifier: String,
        serviceCall: ServiceCall
    ): Task<T> {
        return ExecuteTask(
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

private class ExecuteTask<T>(
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
                    executePersistentCall(this)
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

    private suspend fun executePersistentCall(flow: FlowCollector<T>?): T? {
        val cachedData = serviceCall.getPersistenceDao()?.getResponseByIdentifierAsync(identifier)
        // checked data check and emit
        cachedData?.let { callCache ->
            flow?.let {
                it.emit(Gson().fromJson<T>(callCache.responseSaved, returnType))
            }
        }
        // run network call
        val latestResponse = requestCall.runCatching {
            this.await()
        }.onFailure {
            // on fail to get remote
            throw RequestError("Couldn't reach server")
        }.onSuccess {
            serviceCall.getPersistenceDao()?.updateResponse(
                NetworkCallCache(
                    identifier + uniqueIdentity,
                    Gson().toJson(it),
                    System.currentTimeMillis().toString()
                )
            )
        }
        return latestResponse.getOrNull()
    }
}