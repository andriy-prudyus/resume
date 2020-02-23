package com.andriiprudyus.myresume.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected lateinit var item: T

    protected abstract fun onBindView(item: T)

    fun bindView(item: T) {
        this.item = item
        onBindView(item)
    }
}