package com.bodhi.network.networklayer.base

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import timber.log.Timber

abstract class BasePageAdapter<ResType : Any,
        HoldeType : BaseViewHolder<ResType>>(objectCallback: DiffUtil.ItemCallback<ResType>) :
    PagingDataAdapter<ResType, HoldeType>(
        objectCallback
    ) {

    override fun onBindViewHolder(holder: HoldeType, position: Int) {
        Timber.i(getItem(position).toString())
        getItem(position)?.let {
            holder.populateData(it)
        }
    }
}