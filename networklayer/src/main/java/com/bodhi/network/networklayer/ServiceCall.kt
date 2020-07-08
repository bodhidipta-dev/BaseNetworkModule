package com.bodhi.network.networklayer

import com.bodhi.network.networklayer.local.PersistenceDao
import com.securepreferences.SecurePreferences

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
interface ServiceCall {
    fun getPersistenceDao(): PersistenceDao?
    fun getSecurePreferences(): SecurePreferences
    fun <T> serviceEndpoints(t: Class<T>): T
    fun <T> changeBaseUrl(newUrl: String, serviceEndPoints: Class<T>): T
}