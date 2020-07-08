package com.bodhi.network.networklayer.di

import android.app.Application
import com.bodhi.network.networklayer.NetworkBuilder
import com.bodhi.network.networklayer.NetworkManager
import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.remoteService.networkCall.NetworkImpl
import com.bodhi.network.networklayer.remoteService.networkCall.RemoteCall
import com.bodhi.network.networklayer.ui.dialog.BaseProgressDialog
import com.bodhi.network.networklayer.ui.dialog.DialogImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ActivityBuilder::class
    ]
)
object ActivityModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofitClient(application: Application): ServiceCall {
        return NetworkManager(
            application = application,
            baseurl = "https://techdarz.in/darzi/Apis/",
            networkBuilder = NetworkBuilder(isMock = true)
        ).retrofitclient
    }

    @JvmStatic
    @Provides
    fun providesRemoteCall(serviceCall: ServiceCall): RemoteCall {
        return NetworkImpl(serviceCall)
    }

    @Provides
    @Singleton
    @JvmStatic
    fun proVideProgressLoader(): BaseProgressDialog = DialogImpl()
}