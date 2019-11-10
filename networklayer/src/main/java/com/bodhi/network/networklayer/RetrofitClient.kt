package com.bodhi.network.networklayer

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(
    private val baseurl: String,
    private val networkBuilder: NetworkBuilder = NetworkBuilder(),
    private val retrofitCreator: (retrofit: Retrofit) -> Unit
) {
    private lateinit var retrofit: Retrofit
    private val configuration = object : HTTPConfiguration {
        override fun buildOkHttpClient(okHttpClient: OkHttpClient) {
            retrofit = Retrofit.Builder()
                .apply {
                    client(okHttpClient)
                    baseUrl(baseurl)
                    addConverterFactory(GsonConverterFactory.create())
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                }.build()
            retrofitCreator.invoke(retrofit)
        }

        override fun getNetworkBuilder(): NetworkBuilder = networkBuilder
    }

    init {
        NetworkLayerConfiguration(configuration)
    }

}