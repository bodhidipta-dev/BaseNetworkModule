package com.bodhi.network.networklayer.remoteService

import com.bodhi.network.networklayer.ui.auth.model.AuthRequestModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkEndpoint {
    @POST("getAuthenticate")
    fun authenticateRestaurant(@Body credential: AuthRequestModel?): Observable<ResponseBody>
}