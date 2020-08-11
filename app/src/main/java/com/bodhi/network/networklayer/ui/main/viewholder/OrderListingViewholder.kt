package com.bodhi.network.networklayer.ui.main.viewholder

import android.view.View
import com.bodhi.network.networklayer.base.BaseViewHolder
import com.bodhi.network.networklayer.model.orderDetailsResponse.Order
import com.bodhi.network.networklayer.orderItemClick

sealed class OrderListingViewholder(view: View) : BaseViewHolder<Order>(view)

class PendingOrderListingViewHolder(
    private val view: View,
    private val itemClick: orderItemClick
) : OrderListingViewholder(view) {
    override fun populateData(populatedData: Order) {
        itemView.setOnClickListener { itemClick(absoluteAdapterPosition, populatedData) }
    }
}

class CompletedOrderListingViewHolder(
    private val view: View,
    private val itemClick: orderItemClick
) : OrderListingViewholder(view) {
    override fun populateData(populatedData: Order) {

    }
}