package com.andriiprudyus.myresume.ui.company.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.adapter.BaseViewHolder
import com.andriiprudyus.utils.formattedDate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.list_item_company.view.*

class CompaniesAdapter : RecyclerView.Adapter<CompaniesAdapter.ItemViewHolder>() {

    var actionListener: ActionListener? = null

    private val companies = mutableListOf<Company>()

    interface ActionListener {
        fun onItemClicked(item: Company)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_company, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = companies.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(companies[position])
    }

    fun replaceCompanies(newCompanies: List<Company>) {
        val diffCallback = CompanyDiffCallback(companies, newCompanies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        companies.apply {
            clear()
            addAll(newCompanies)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemViewHolder(itemView: View) :
        BaseViewHolder<Company>(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onBindView(item: Company) {
            itemView.apply {
                companyNameTextView.text = item.companyName
                roleName.text = item.roleName

                durationTextView.text = "%s - %s".format(
                    formattedDate(item.startedAt ?: 0),
                    formattedDate(item.endedAt ?: 0)
                )

                Glide.with(context)
                    .setDefaultRequestOptions(
                        RequestOptions().error(R.drawable.ic_launcher_background)
                    )
                    .load(item.logoUrl)
                    .into(logoImageView)
            }
        }

        override fun onClick(v: View?) {
            actionListener?.onItemClicked(item)
        }
    }
}