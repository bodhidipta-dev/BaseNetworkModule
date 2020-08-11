package com.bodhi.network.networklayer.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bodhi.network.networklayer.R
import com.bodhi.network.networklayer.base.BaseActivity
import com.bodhi.network.networklayer.ui.main.DashBoardActivity
import kotlinx.android.synthetic.main.activity_auth_activity.*
import kotlinx.coroutines.flow.collectLatest

class AuthenticationActivity : BaseActivity<AuthenticationViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_activity)
        viewmodel =
            ViewModelProvider(this, viewModelFactory).get(AuthenticationViewModel::class.java)

        proceed.setOnClickListener {
            if (userId.text.toString().isEmpty() ||
                password_text.text.toString().isEmpty()
            ) {
                if (userId.text.toString().isEmpty())
                    userId_layout.error = "Please enter Valid Id"
                if (password_text.text.toString().isEmpty())
                    password_layout.error = "Please enter Password"
            } else {
                userId_layout.error = null
                password_layout.error = null
                showProgress()
                lifecycleScope.launchWhenResumed {
                    viewmodel.authenticateUser(
                        userId.text.toString(),
                        password_text.text.toString()
                    ).executeFlow(lifecycleScope).collectLatest {
                        dismissProgressLoader()
                        preferenceManager.putSessionID(it.response.accessToken)
                        startActivity(
                            Intent(
                                this@AuthenticationActivity,
                                DashBoardActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }
}