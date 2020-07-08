package com.bodhi.network.networklayer.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<R, T : BaseViewHolder<R>>(private var item: List<R>) :
    RecyclerView.Adapter<T>() {

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.populateData(item[position])
    }

    fun updateData(list: List<R>) {
        item = list
        notifyDataSetChanged()
    }
}