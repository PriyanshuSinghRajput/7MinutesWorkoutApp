package com.priyanshu.a7minutesworkoutapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.priyanshu.a7minutesworkoutapp.databinding.HistoryDateItemLayoutBinding

class HistoryAdapter(private val list: List<HistoryEntity>):
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

        inner class HistoryViewHolder(itemBinding: HistoryDateItemLayoutBinding):
            RecyclerView.ViewHolder(itemBinding.root){
                var llItem = itemBinding.llItem
                var index = itemBinding.tvItemIndex
                var date = itemBinding.tvItemDate
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(HistoryDateItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = list[position]
        val index = position + 1
        val context = holder.itemView.context

        holder.date.text  = item.date
        holder.index.text = index.toString()

        if(index%2 == 0){
            holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGray))
        }
        else{
            holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
