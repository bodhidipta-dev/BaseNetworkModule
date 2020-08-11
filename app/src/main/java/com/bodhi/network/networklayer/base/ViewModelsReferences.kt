package com.bodhi.network.networklayer.base

import com.bodhi.network.networklayer.ui.main.MainViewModel
import com.bodhi.network.networklayer.ui.auth.AuthenticationViewModel

enum class ViewModelsReferences(val value: Class<*>) {
    SPLASH(AuthenticationViewModel::class.java),
    PRODUUCT(AuthenticationViewModel::class.java),
    MAIN(MainViewModel::class.java)
}

enum class ViewModelsReferencesSingleton(val value: Class<*>) {
    DASHBOARD(AuthenticationViewModel::class.java) // change it with desired one
}