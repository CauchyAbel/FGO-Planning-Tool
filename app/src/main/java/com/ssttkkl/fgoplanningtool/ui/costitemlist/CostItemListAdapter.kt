package com.ssttkkl.fgoplanningtool.ui.costitemlist

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ssttkkl.fgoplanningtool.R
import com.ssttkkl.fgoplanningtool.data.item.Item
import com.ssttkkl.fgoplanningtool.data.item.ItemsRepository
import com.ssttkkl.fgoplanningtool.utils.toStringWithSplitter
import kotlinx.android.synthetic.main.item_costitemlist.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class CostItemListAdapter(val context: Context) : RecyclerView.Adapter<CostItemListAdapter.ViewHolder>() {
    var data: List<CostItemListEntity> = listOf()
        private set

    fun setNewData(items: Collection<Item>) {
        runBlocking(CommonPool) {
            data = items.sortedBy { it.descriptor?.type }
                    .map {
                        CostItemListEntity(it.descriptor?.localizedName ?: it.codename,
                                it.descriptor?.type?.localizedName ?: "",
                                it.count,
                                ItemsRepository.get(it.codename).count,
                                it.descriptor?.imgUri ?: "")
                    }
        }
        launch(UI) { notifyDataSetChanged() }
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_costitemlist, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val item = data[pos]
        holder.itemView.apply {
            // setup a subhead if this item is the head of its group
            if (pos == 0 || (data[pos - 1].type != data[pos].type)) {
                divider.visibility = if (pos != 0) View.VISIBLE else View.GONE
                type_textView.visibility = View.VISIBLE
                type_textView.text = item.type
            } else {
                divider.visibility = View.GONE
                type_textView.visibility = View.GONE
            }

            name_textView.text = item.name
            require_textView.text = item.need.toStringWithSplitter()
            own_textView.text = item.own.toStringWithSplitter()

            if (item.need > item.own) {
                own_textView.setTextColor(ResourcesCompat.getColor(context.resources, android.R.color.holo_red_light, null))
                own_textView.typeface = Typeface.DEFAULT_BOLD
            } else {
                own_textView.setTextColor(ResourcesCompat.getColor(context.resources, android.R.color.primary_text_light, null))
                own_textView.typeface = Typeface.DEFAULT
            }

            Glide.with(context).load(item.imgUri).into(avatar_imageView)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}