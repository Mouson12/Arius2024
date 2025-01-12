package com.example.garage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for managing and displaying a list of repair history items in a RecyclerView.
 *
 * @param items List of `RepairHistory` objects representing the user's repair history.
 * @param onRateClick Lambda function triggered when the "Rate" button is clicked.
 *        Passes the `repair_order_id` and workshop name to the caller.
 */
class RepairHistoryAdapter(
    private var items: List<RepairHistory>,
    private val onRateClick: (Int, String) -> Unit
) : RecyclerView.Adapter<RepairHistoryAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding references to the views in a single repair history item.
     *
     * @param itemView The root view of the repair history item layout.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.description) // Repair report
        val date: TextView = itemView.findViewById(R.id.date) // Completion date
        val rateButton: Button = itemView.findViewById(R.id.rateButton) // Button for rating
    }

    /**
     * Called when a new ViewHolder is created. Inflates the layout for a single item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new `ViewHolder` instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repair_history, parent, false)
        return ViewHolder(view)
    }

    /**
     * Called to bind data to the specified ViewHolder.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the dataset.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set repair description and completion date
        holder.description.text = item.report
        holder.date.text = item.completed_at

        // Set click listener for the "Rate" button
        holder.rateButton.setOnClickListener {
            onRateClick(item.repair_order_id, "Workshop Name") // Pass `repair_order_id` and a placeholder workshop name
        }
    }

    /**
     * Returns the total number of items in the dataset.
     *
     * @return The size of the items list.
     */
    override fun getItemCount() = items.size

    /**
     * Updates the dataset with new repair history items and refreshes the RecyclerView.
     *
     * @param newItems The new list of `RepairHistory` objects.
     */
    fun updateData(newItems: List<RepairHistory>) {
        items = newItems
        notifyDataSetChanged()
    }
}