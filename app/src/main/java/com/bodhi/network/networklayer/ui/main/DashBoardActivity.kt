package com.bodhi.network.networklayer.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bodhi.network.networklayer.R
import com.bodhi.network.networklayer.base.BaseActivity
import com.bodhi.network.networklayer.ui.main.adapter.PagingOrderAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn

class DashBoardActivity : BaseActivity<MainViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewmodel =
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        demo_list_view.layoutManager = LinearLayoutManager(this)
        val pageAdapter = PagingOrderAdapter() { position, populatedData ->
            /* Do something here */
        }
        demo_list_view.adapter = pageAdapter

        viewmodel.getPendingOrder(preferenceManager.getSessionId() ?: "mockuthtoken1")
        viewmodel.getPendingLow()?.apply {
            launchIn(lifecycleScope)
            lifecycleScope.launchWhenCreated {
                collectLatest {
                    /* Do work with data */
                    /* demo_list_view.adapter = OrderItemAdapter(it){
                         position: Int, populatedData: Order ->
                         *//* Do some delegate action*//*
                    }*/
                }
            }
        }
        viewmodel.getPendingPagination(preferenceManager.getSessionId() ?: "").apply {
            launchIn(lifecycleScope)
            lifecycleScope.launchWhenCreated {
                collectLatest {
// Submit data for paging
                    pageAdapter.submitData(it)
                }
            }
        }

    }
}