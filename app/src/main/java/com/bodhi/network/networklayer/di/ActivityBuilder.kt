package com.bodhi.network.networklayer.di

import com.bodhi.network.networklayer.ui.auth.AuthenticationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): AuthenticationActivity
}