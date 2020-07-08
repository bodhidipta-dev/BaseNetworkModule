package com.bodhi.network.networklayer.ui.dialog

import android.content.Context
import androidx.annotation.StringRes

interface BaseProgressDialog {
    fun showLoader(context: Context, cancellable: Boolean)
    fun dismissLoader()
    fun showDialog(
        context: Context,
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes positiveButton: Int?,
        @StringRes negativeButton: Int?,
        positiveButtonDelegate: () -> Unit = {},
        negativeButtonDelegate: () -> Unit = {}
    )

    fun showNetworkError(context: Context, positiveButtonDelegate: () -> Unit = {})
}