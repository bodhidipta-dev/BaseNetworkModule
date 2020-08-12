package com.bodhi.network.networklayer.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bodhi.network.networklayer.NetworkManager
import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.config.NetworkBuilder
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.remoteService.networkCall.NetworkImpl
import com.bodhi.network.networklayer.ui.auth.AuthenticationActivity
import com.bodhi.network.networklayer.ui.auth.AuthenticationViewModel
import com.bodhi.network.networklayer.ui.dialog.BaseProgressDialog
import com.bodhi.network.networklayer.ui.dialog.DialogImpl
import com.bodhi.network.networklayer.ui.main.DashBoardActivity
import com.bodhi.network.networklayer.ui.main.MainViewModel
import com.essarsystems.orderingsystems.base.BaseViewModelFactory
import com.essarsystems.orderingsystems.base.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ActivityBuilder {
    @Module
    companion object ActivityModule {
        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofitClient(application: Application): ServiceCall {
            return NetworkManager(
                application = application,
                baseurl = "https://api.somepi.com/",
                networkBuilder = NetworkBuilder(
                    isMock = true,
                    mockKye = "Authorisation",
                    authenticator = {
                        /* Check request here before call request
                        * */
                        /*if(it.request.headers["expiredTime"]?.isExpired() == true)
                            //Do get new access token with refresh
                            //token or place another api call*/
                        it.request.newBuilder().build()
                    },
                    cachePolicy = true,
                    shouldUseInterceptor = true, // To use for BODY HttpLoggingInterceptor
                    shouldUseChuckInterceptor = true, // To use Chuck Interceptor
                    interceptor = {
                        /*do your stuff*/
                        it // Change or modify your response before proceed
                    },
                    requestHeaders = mapOf(), // Place your headers added
                    // explicitly without placing every service call
                    // like example mapof("deviceID" to "xxxx", "geoLocation" to "latLong", "blah .." to "blah blah ..")
                    retryPolicy = true, // When it should retry in case of failure
                    timeoutInMillis = 10000L // Timeout for service call
                )//header key
            ).retrofitclient
        }

        @JvmStatic
        @Provides
        fun providesNetwork(serviceCall: ServiceCall): NetworkEndpoint =
            serviceCall.serviceEndpoints(NetworkEndpoint::class.java)

        @JvmStatic
        @Provides
        @Singleton
        fun providesRemoteCall(
            serviceCall: ServiceCall,
            network: NetworkEndpoint
        ): NetworkImpl {
            return NetworkImpl(serviceCall, network)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun proVideProgressLoader(): BaseProgressDialog = DialogImpl()
    }

    @Binds
    internal abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): AuthenticationActivity

    @ContributesAndroidInjector
    abstract fun bindDashBoardActivity(): DashBoardActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun provideAuthenticationViewModel(viewModel: AuthenticationViewModel): ViewModel

    @Binds
    internal abstract fun postRemoteCall(networkImpl: NetworkImpl): RemoteCall

}