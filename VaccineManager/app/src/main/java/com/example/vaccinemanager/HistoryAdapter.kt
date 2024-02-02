package com.example.vaccinemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinemanager.firestore.FireStoreData

/**
 * HistoryAdapter is a RecyclerView adapter responsible for populating vaccination history data.
 *
 * @property vaccinationHistory List containing vaccination history data.
 */
class HistoryAdapter(private val vaccinationHistory: List<FireStoreData>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    /**
     * Represents a single item view in the RecyclerView.
     *
     * @property textVaccineName TextView to display the vaccine name.
     * @property textDate TextView to display the vaccination date.
     * @property textTime TextView to display the vaccination time.
     * @property textAddress TextView to display the vaccination address.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textVaccineName: TextView = itemView.findViewById(R.id.tvVaccineNameItem)
        val textDate: TextView = itemView.findViewById(R.id.tvDateItem)
        val textTime: TextView = itemView.findViewById(R.id.tvTimeItem)
        val textAddress: TextView = itemView.findViewById(R.id.tvAddressItem)
    }

    /**
     * Creates ViewHolder instances for each item view in the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds data to the ViewHolder views for each item in the RecyclerView.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccination = vaccinationHistory[position]
        holder.textVaccineName.text = vaccination.vaccineName
        holder.textDate.text = vaccination.date
        holder.textTime.text = vaccination.time
        holder.textAddress.text = vaccination.address
    }

    /**
     * Returns the total number of items in the RecyclerView.
     */
    override fun getItemCount(): Int {
        return vaccinationHistory.size
    }
}