package com.bodhi.network.networklayer

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(val retrofitCreator: (retrofit: Retrofit) -> Unit) : HTTPConfiguration {
    private lateinit var retrofit: Retrofit
    override fun buildOkHttpClient(okHttpClient: OkHttpClient) {
        retrofit = Retrofit.Builder()
            .apply {
                client(okHttpClient)
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            }.build().also { retrofitCreator.invoke(it) }
    }

    override fun getNetworkBuilder(): NetworkBuilder = NetworkBuilder()
}