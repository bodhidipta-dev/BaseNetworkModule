package com.bodhi.network.networklayer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bodhi.network.networklayer.R
import com.bodhi.network.networklayer.base.BasePageAdapter
import com.bodhi.network.networklayer.model.orderDetailsResponse.Order
import com.bodhi.network.networklayer.orderItemClick
import com.bodhi.network.networklayer.ui.main.viewholder.OrderListingViewholder
import com.bodhi.network.networklayer.ui.main.viewholder.PendingOrderListingViewHolder

class PagingOrderAdapter(
    private val clickListener: orderItemClick
) : BasePageAdapter<Order, OrderListingViewholder>(
    objectCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(
            oldItem: Order,
            newItem: Order
        ): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(
            oldItem: Order,
            newItem: Order
        ): Boolean {
            return !oldItem.equals(newItem)
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListingViewholder {
        return PendingOrderListingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item_orderdetails, parent, false),
                clickListener
            )
    }
}