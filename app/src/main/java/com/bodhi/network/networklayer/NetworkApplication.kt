package com.bodhi.network.networklayer

import com.bodhi.network.networklayer.di.ApplicationModule
import com.bodhi.network.networklayer.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class NetworkApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(
            this,
            ApplicationModule(this)
        )
    }
}