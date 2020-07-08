package com.bodhi.network.networklayer

import okhttp3.OkHttpClient

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
interface HTTPConfiguration {
    fun buildOkHttpClient(okHttpClient: OkHttpClient)
    fun getNetworkBuilder(): NetworkBuilder
}