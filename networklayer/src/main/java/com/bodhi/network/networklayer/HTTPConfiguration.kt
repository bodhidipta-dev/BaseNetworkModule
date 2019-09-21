package com.bodhi.network.networklayer

import okhttp3.OkHttpClient

interface HTTPConfiguration {
    fun buildOkHttpClient(okHttpClient: OkHttpClient)
    fun getNetworkBuilder(): NetworkBuilder
}