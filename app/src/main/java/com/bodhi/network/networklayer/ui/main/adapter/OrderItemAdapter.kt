package com.bodhi.network.networklayer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bodhi.network.networklayer.R
import com.bodhi.network.networklayer.base.BaseAdapter
import com.bodhi.network.networklayer.model.orderDetailsResponse.Order
import com.bodhi.network.networklayer.orderItemClick
import com.bodhi.network.networklayer.ui.main.viewholder.OrderListingViewholder
import com.bodhi.network.networklayer.ui.main.viewholder.PendingOrderListingViewHolder

class OrderItemAdapter(
    private val list: List<Order>,
    private val clickOrder: orderItemClick = { position, order -> }
) :
    BaseAdapter<Order, OrderListingViewholder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListingViewholder {
        return PendingOrderListingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_item_orderdetails,
                    parent,
                    false
                ), clickOrder
        )
    }
}