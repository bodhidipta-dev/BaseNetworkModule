package com.bodhi.network.networklayer

import android.app.Application
import com.bodhi.network.networklayer.local.LocalDataBase
import com.bodhi.network.networklayer.local.PersistenceDao
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.securepreferences.SecurePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
class NetworkManager(
    private val application: Application,
    private val baseurl: String,
    private val networkBuilder: NetworkBuilder = NetworkBuilder()
) {
    private lateinit var retrofit: Retrofit
    lateinit var retrofitclient: ServiceCall
    private var localDataBase: PersistenceDao? =
        LocalDataBase.getInstance(application)?.getLocalPreferenceDAO()
    private val configuration = object : HTTPConfiguration {
        override fun buildOkHttpClient(okHttpClient: OkHttpClient) {
            retrofit = Retrofit.Builder()
                .apply {
                    client(okHttpClient)
                    baseUrl(baseurl)
                    addConverterFactory(GsonConverterFactory.create())
                    addCallAdapterFactory(CoroutineCallAdapterFactory())
                }.build()
            retrofitclient = ServiceCallImpl(
                retrofit,
                localDataBase,
                SecurePreferences(
                    application,
                    "com.bodhi.network1@919"
                )
            )
        }

        override fun getNetworkBuilder(): NetworkBuilder = networkBuilder
    }

    init {
        NetworkLayerConfiguration(application, configuration)
        CoroutineScope(Dispatchers.IO).launch {
            localDataBase?.clearResponse()
            Timber.i("Executing on the context: ->${this.coroutineContext}")
        }
    }

}