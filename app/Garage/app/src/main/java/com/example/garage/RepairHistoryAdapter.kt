package com.example.garage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepairHistoryAdapter(private var items: List<RepairHistory>) :
    RecyclerView.Adapter<RepairHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.description)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repair_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.description.text = item.report // Użyj pola "report" z modelu
        holder.date.text = item.completed_at // Użyj pola "completed_at" z modelu
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<RepairHistory>) {
        items = newItems
        notifyDataSetChanged()
    }
}