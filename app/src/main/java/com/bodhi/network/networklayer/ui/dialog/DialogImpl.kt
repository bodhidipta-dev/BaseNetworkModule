package com.bodhi.network.networklayer.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.bodhi.network.networklayer.R
import kotlinx.android.synthetic.main.dialog_full_screen_loader.view.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DialogImpl @Inject constructor() : BaseProgressDialog {
    private var fullscreenLoader: Dialog? = null
    private var alertDialog: AlertDialog? = null
    override fun showLoader(context: Context, cancellable: Boolean) {
        if (fullscreenLoader?.isShowing == true)
            fullscreenLoader?.dismiss()

        val progrDialogView = LayoutInflater.from(context).inflate(
            R.layout.dialog_full_screen_loader,
            null
        )
        progrDialogView.loading_progress.addValueCallback(
            KeyPath("**"),
            LottieProperty.COLOR,
            { frameInfo -> Color.DKGRAY }
        )
        fullscreenLoader = AppCompatDialog(context)
        fullscreenLoader?.setContentView(
            progrDialogView
        )
        fullscreenLoader?.setCancelable(cancellable)
        fullscreenLoader?.setCanceledOnTouchOutside(false)
        fullscreenLoader?.show()


    }

    override fun dismissLoader() {
        if (fullscreenLoader?.isShowing == true)
            fullscreenLoader?.dismiss()
    }

    override fun showDialog(
        context: Context,
        title: Int,
        message: Int,
        positiveButton: Int?,
        negativeButton: Int?,
        positiveButtonDelegate: () -> Unit,
        negativeButtonDelegate: () -> Unit
    ) {
        if (alertDialog?.isShowing == true)
            alertDialog?.dismiss()

        alertDialog = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(title))
            setMessage(context.resources.getString(message))
            setCancelable(false)
            setPositiveButton(
                positiveButton ?: R.string.ok
            ) { dialog, which ->
                dialog.dismiss()
                positiveButtonDelegate()
            }
            negativeButton?.let {
                setNegativeButton(it) { dialog, which ->
                    dialog.dismiss()
                    negativeButtonDelegate()
                }
            }
        }.show()
    }

    override fun showNetworkError(
        context: Context,
        positiveButtonDelegate: () -> Unit
    ) {
        if (alertDialog?.isShowing == true)
            alertDialog?.dismiss()

        alertDialog = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.title_network_error))
            setMessage(context.resources.getString(R.string.message_network_error))
            setCancelable(false)
            setPositiveButton(
                R.string.ok
            ) { dialog, which ->
                dialog.dismiss()
                positiveButtonDelegate()
            }
        }.show()
    }

}