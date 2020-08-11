package com.bodhi.network.networklayer.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bodhi.network.networklayer.NetworkBuilder
import com.bodhi.network.networklayer.NetworkManager
import com.bodhi.network.networklayer.RemoteCall
import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.preference.PreferenceManager
import com.bodhi.network.networklayer.remoteService.NetworkEndpoint
import com.bodhi.network.networklayer.remoteService.networkCall.NetworkImpl
import com.bodhi.network.networklayer.remoteService.services.PendingServicesScope
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
                    mockKye = "Authorisation"
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