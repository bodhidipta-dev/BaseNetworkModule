package com.bodhi.network.networklayer

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(
    private val baseurl: String,
    val retrofitCreator: (retrofit: Retrofit) -> Unit
) : HTTPConfiguration {
    private lateinit var retrofit: Retrofit
    init {
        NetworkLayerConfiguration(this)
    }
    override fun buildOkHttpClient(okHttpClient: OkHttpClient) {
        retrofit = Retrofit.Builder()
            .apply {
                client(okHttpClient)
                baseUrl(baseurl)
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            }.build().also {
                retrofitCreator.invoke(it)
            }
    }

    override fun getNetworkBuilder(): NetworkBuilder = NetworkBuilder()
}