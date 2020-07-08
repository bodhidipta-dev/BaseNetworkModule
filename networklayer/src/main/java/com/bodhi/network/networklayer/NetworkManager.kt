package com.bodhi.network.networklayer

import android.app.Application
import com.bodhi.network.networklayer.local.LocalDataBase
import com.bodhi.network.networklayer.local.PersistenceDao
import com.securepreferences.SecurePreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
        NetworkLayerConfiguration(application,configuration)
        localDataBase?.clearResponse()?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.doOnComplete {
                Timber.i("Local db cleared")
            }?.doOnError {
                Timber.i("Error while clearing db..")
            }
            ?.subscribe()
    }

}