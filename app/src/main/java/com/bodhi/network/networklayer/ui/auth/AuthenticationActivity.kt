package com.bodhi.network.networklayer.ui.auth

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bodhi.network.networklayer.R
import com.bodhi.network.networklayer.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth_activity.*

class AuthenticationActivity : BaseActivity<AuthenticationViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_activity)
        viewmodel =
            ViewModelProvider(this, viewModelFactory).get(AuthenticationViewModel::class.java)
        viewmodel.authenticationObservable.observe(this,
            Observer { response ->
                dismissProgressLoader()
                response?.let {
                    //do something
                }
            })
        proceed.setOnClickListener {
            if (userId.text.toString().isEmpty() ||
                password_text.text.toString().isEmpty()
            ) {
                if (userId.text.toString().isEmpty())
                    userId_layout.error = "Please enter Restaurant Id"
                if (password_text.text.toString().isEmpty())
                    password_layout.error = "Please enter Password"
            } else {
                userId_layout.error = null
                password_layout.error = null
                showProgress()
                viewmodel.authenticateUser(
                    userId.text.toString(),
                    password_text.text.toString()
                )
            }
        }
    }
}