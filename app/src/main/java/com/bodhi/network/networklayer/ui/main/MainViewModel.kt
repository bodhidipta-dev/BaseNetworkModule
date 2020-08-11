package com.bodhi.network.networklayer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.local.NetworkSyncData
import com.bodhi.network.networklayer.model.orderDetailsResponse.Order
import com.bodhi.network.networklayer.model.orderDetailsResponse.OrderDetailsResponse
import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import com.bodhi.network.networklayer.remoteService.services.PendingServicesScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var remoteCall: RemoteCall

    fun getPendingLow() = remoteCall.getPersistenceDAO()?.getAllSyncedDataFlow("Pending")
        ?.map {
            it.sortedByDescending {
                Date(it.syncedTime)
            }.map {
                Gson().fromJson(it.responseSaved, Order::class.java)
            }
        }

    fun deletePendingOrders() {
        viewModelScope.launch {
            remoteCall.getPersistenceDAO()
                ?.deleteAllSyncData("Pending")
        }
    }

    fun getPendingOrder(sessionId: String) {
        viewModelScope.launch {
            remoteCall.getProxyTask<OrderDetailsResponse>(
                mapOf(PROXY_DATA to sessionId),
                ServiceIdentifier.PENDING_ORDER.name
            ).executeFlow(this)
                .catch {
                    deletePendingOrders()
                }
                .collectLatest {
                    if (it.orderList.isNotEmpty()) {
                        remoteCall.getPersistenceDAO()?.insertAllSyncData(it.orderList.map {
                            NetworkSyncData(
                                it.orderId,
                                "Pending",
                                Gson().toJson(it),
                                System.currentTimeMillis()
                            )
                        })
                    }

                }
        }
    }

    fun getPendingPagination(accessToken: String) = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 5, enablePlaceholders = true)
    )
    {
        PendingServicesScope(remoteCall, accessToken)
    }.flow
}