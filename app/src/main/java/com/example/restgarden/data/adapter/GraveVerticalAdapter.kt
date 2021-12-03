package com.example.restgarden.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.GraveVerticalViewHolder
import com.example.restgarden.data.model.Grave

class GraveVerticalAdapter(private val graveList: List<Grave>) :
  RecyclerView.Adapter<GraveVerticalViewHolder>(), Filterable {
  
  var graveFilterList = ArrayList<Grave>()
  
  init {
    graveFilterList = graveList as ArrayList<Grave>
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraveVerticalViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_grave_vertical, parent, false)
    return GraveVerticalViewHolder(view)
  }
  
  override fun onBindViewHolder(holderHorizontal: GraveVerticalViewHolder, position: Int) {
    holderHorizontal.bind(graveFilterList[position])
  }
  
  override fun getItemCount(): Int = graveFilterList.size
  
  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        graveFilterList = if (charSearch.trim().isBlank()) graveList as ArrayList<Grave>
        else graveList.filter {
          it.name.lowercase().contains(charSearch.lowercase()) || it.address.lowercase()
            .contains(charSearch.lowercase())
        } as ArrayList<Grave>
        val filterResult = FilterResults()
        filterResult.values = graveFilterList
        return filterResult
      }
      
      @SuppressLint("NotifyDataSetChanged")
      override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        graveFilterList = results?.values as ArrayList<Grave>
        notifyDataSetChanged()
      }
      
    }
  }
}
