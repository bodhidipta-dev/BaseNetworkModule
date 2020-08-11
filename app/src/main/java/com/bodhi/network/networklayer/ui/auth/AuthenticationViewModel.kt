package com.bodhi.network.networklayer.ui.auth

import androidx.lifecycle.ViewModel
import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.proxy.PROXY_DATA
import com.bodhi.network.networklayer.remoteService.ServiceIdentifier
import com.bodhi.network.networklayer.ui.auth.model.AuthRequestModel
import com.bodhi.network.networklayer.ui.auth.model.model.AuthResponse
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(val remoteCall: RemoteCall) : ViewModel() {
    fun authenticateUser(restaurantId: String, password: String) =
        remoteCall.getProxyTask<AuthResponse>(
            mapOf(PROXY_DATA to AuthRequestModel(restaurantId, password)),
            ServiceIdentifier.AUTHENTICATION.name
        )
}