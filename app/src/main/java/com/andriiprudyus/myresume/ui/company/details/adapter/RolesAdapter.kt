package com.andriiprudyus.myresume.ui.company.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.adapter.BaseViewHolder
import com.andriiprudyus.myresume.utils.formattedDate
import kotlinx.android.synthetic.main.list_item_company_details_achievement.view.*
import kotlinx.android.synthetic.main.list_item_company_details_responsibility.view.*
import kotlinx.android.synthetic.main.list_item_company_details_role.view.*
import kotlinx.android.synthetic.main.list_item_company_details_summary.view.*

class RolesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SUMMARY = 1
        private const val TYPE_ROLE = 2
        private const val TYPE_RESPONSIBILITY = 3
        private const val TYPE_ACHIEVEMENT = 4
    }

    private val items = mutableListOf<Item>()

    sealed class Item {

        data class Summary(
            val summary: String
        ) : Item()

        data class Role(
            val roleName: String,
            val startedAt: Long,
            val endedAt: Long
        ) : Item()

        data class Responsibility(
            val responsibilityName: String
        ) : Item()

        data class Achievement(
            val achievementName: String
        ) : Item()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.Summary -> TYPE_SUMMARY
            is Item.Role -> TYPE_ROLE
            is Item.Responsibility -> TYPE_RESPONSIBILITY
            is Item.Achievement -> TYPE_ACHIEVEMENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_SUMMARY -> {
                SummaryViewHolder(
                    inflater.inflate(R.layout.list_item_company_details_summary, parent, false)
                )
            }
            TYPE_ROLE -> {
                RoleViewHolder(
                    inflater.inflate(R.layout.list_item_company_details_role, parent, false)
                )
            }
            TYPE_RESPONSIBILITY -> {
                ResponsibilityViewHolder(
                    inflater.inflate(
                        R.layout.list_item_company_details_responsibility,
                        parent,
                        false
                    )
                )
            }
            TYPE_ACHIEVEMENT -> {
                AchievementViewHolder(
                    inflater.inflate(R.layout.list_item_company_details_achievement, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Item.Summary -> (holder as SummaryViewHolder).bindView(item)
            is Item.Role -> (holder as RoleViewHolder).bindView(item)
            is Item.Responsibility -> {
                (holder as ResponsibilityViewHolder).apply {
                    showTitle = items[position - 1] !is Item.Responsibility
                    bindView(item)
                }
            }
            is Item.Achievement -> {
                (holder as AchievementViewHolder).apply {
                    showTitle = items[position - 1] !is Item.Achievement
                    lastItem = position == items.size - 1
                    bindView(item)
                }
            }
        }
    }

    fun replaceItems(newItems: List<Item>) {
        items.apply {
            clear()
            addAll(newItems)
            notifyDataSetChanged()
        }
    }

    inner class SummaryViewHolder(itemView: View) : BaseViewHolder<Item.Summary>(itemView) {

        override fun onBindView(item: Item.Summary) {
            itemView.summaryValueTextView.text = item.summary
        }
    }

    inner class RoleViewHolder(itemView: View) : BaseViewHolder<Item.Role>(itemView) {

        override fun onBindView(item: Item.Role) {
            itemView.apply {
                roleNameTextView.text = item.roleName

                durationTextView.text = "%s - %s".format(
                    formattedDate(item.startedAt),
                    formattedDate(item.endedAt)
                )
            }
        }
    }

    inner class ResponsibilityViewHolder(itemView: View) :
        BaseViewHolder<Item.Responsibility>(itemView) {

        var showTitle = false

        override fun onBindView(item: Item.Responsibility) {
            itemView.apply {
                responsibilitiesTextView.isVisible = showTitle
                responsibilityNameTextView.text = "- %s".format(item.responsibilityName)
            }
        }
    }

    inner class AchievementViewHolder(itemView: View) : BaseViewHolder<Item.Achievement>(itemView) {

        var showTitle = false
        var lastItem = false

        override fun onBindView(item: Item.Achievement) {
            itemView.apply {
                achievementsTextView.isVisible = showTitle

                achievementNameTextView.apply {
                    text = "- %s".format(item.achievementName)

                    setPadding(
                        paddingLeft,
                        paddingTop,
                        paddingRight,
                        if (lastItem) resources.getDimension(R.dimen.base_margin).toInt() else 0
                    )
                }
            }
        }
    }
}