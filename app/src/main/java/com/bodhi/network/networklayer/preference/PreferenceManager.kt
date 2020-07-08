package com.bodhi.network.networklayer.preference

import android.app.Application
import com.bodhi.network.networklayer.BuildConfig
import com.securepreferences.SecurePreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferenceManager @Inject constructor(application: Application) {
    private val securePrefs: SecurePreferences = SecurePreferences(
        application,
        BuildConfig.SEC_TOKEN
    )

    fun getSessionId(): String? =
        securePrefs.getString(KepParams.SESSION.value, "")

}