package com.bodhi.network.networklayer.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bodhi.network.networklayer.preference.PreferenceManager
import com.bodhi.network.networklayer.ui.dialog.BaseProgressDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

internal const val TYPE_PARAMS = "Type"
internal const val PARCELABLE_DATA = "BundleData"

open class BaseFragment<T : ViewModel> : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferenceManager: PreferenceManager
    lateinit var viewmodel: T

    @Inject
    lateinit var dialogManger: BaseProgressDialog

    fun showProgress(cancellable: Boolean = false) {
        activity?.let { dialogManger.showLoader(it, cancellable) }
    }

    fun dismissProgressLoader() {
        dialogManger.dismissLoader()
    }

    override fun onDestroy() {
        dismissProgressLoader()
        super.onDestroy()
    }
}