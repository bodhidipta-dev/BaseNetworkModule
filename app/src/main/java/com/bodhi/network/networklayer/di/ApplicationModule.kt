package com.bodhi.network.networklayer.di

import android.app.Application
import com.bodhi.network.networklayer.NetworkApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val applicationContext: NetworkApplication) {
    @Provides
    fun provideApplicationContext(): Application = applicationContext
}