package com.andriiprudyus.myresume.ui.company.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.andriiprudyus.myresume.db.company.Company

class CompanyDiffCallback(
    private val oldList: List<Company>,
    private val newList: List<Company>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].companyName == newList[newItemPosition].companyName
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}