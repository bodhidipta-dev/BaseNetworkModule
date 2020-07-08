package com.bodhi.network.networklayer.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.bodhi.network.networklayer.preference.PreferenceManager
import com.bodhi.network.networklayer.ui.dialog.BaseProgressDialog
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

const val SCREENDATA = "IntentScreenData"

abstract class BaseActivity<T : ViewModel> : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory

    @Inject
    lateinit var preferenceManager: PreferenceManager
    lateinit var viewmodel: T

    @Inject
    lateinit var dialogManger: BaseProgressDialog

    @Inject
    lateinit var progressDialog: BaseProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun showProgress(cancellable: Boolean = false) {
        progressDialog.showLoader(this, cancellable)
    }

    fun dismissProgressLoader() {
        progressDialog.dismissLoader()
    }

    override fun onDestroy() {
        dismissProgressLoader()
        super.onDestroy()
    }
}