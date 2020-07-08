package com.bodhi.network.networklayer.di

import android.content.Context
import com.bodhi.network.networklayer.NetworkApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        ApplicationModule::class,
        AndroidSupportInjectionModule::class]
)
interface ApplicationComponent : AndroidInjector<NetworkApplication> {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Context,
            appliacation: ApplicationModule
        ): ApplicationComponent
    }
}