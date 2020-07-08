package com.bodhi.network.networklayer.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import com.bodhi.network.networklayer.remoteService.networkCall.RemoteCall
import com.bodhi.network.networklayer.ui.auth.model.AuthRequestModel
import com.bodhi.network.networklayer.ui.auth.model.model.AuthResponse

class AuthenticationViewModel(private val remoteCall: RemoteCall) : ViewModel() {
    val authenticationObservable = MutableLiveData<AuthResponse>()
    fun authenticateUser(restaurantId: String, password: String) {
        remoteCall.getProxyTask<AuthResponse>(
            mapOf(PROXY_DATA to AuthRequestModel(restaurantId, password)),
            ServiceIdentifier.AUTHENTICATION
        ).execute(onsuccess = {
            authenticationObservable.postValue(it)
        }, onerror = {
            authenticationObservable.postValue(null)
        })
    }
}