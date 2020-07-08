package com.bodhi.network.networklayer

import com.bodhi.network.networklayer.local.PersistenceDao
import com.securepreferences.SecurePreferences
import retrofit2.Retrofit

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
class ServiceCallImpl(
    private val retrofit: Retrofit,
    private val localPreferenceDAO: PersistenceDao?,
    private val securePreferences: SecurePreferences
) : ServiceCall {
    override fun getPersistenceDao(): PersistenceDao? = localPreferenceDAO
    override fun getSecurePreferences(): SecurePreferences = securePreferences

    override fun <T> serviceEndpoints(t: Class<T>): T {
        return retrofit.create(t)
    }

    override fun <T> changeBaseUrl(newUrl: String, serviceEndPoints: Class<T>): T {
        return retrofit.newBuilder().baseUrl(newUrl).build().create(serviceEndPoints)
    }
}