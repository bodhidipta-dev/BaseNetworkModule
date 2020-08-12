package com.bodhi.network.networklayer.proxy

import androidx.paging.PagingSource
import retrofit2.HttpException
import java.io.IOException

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
