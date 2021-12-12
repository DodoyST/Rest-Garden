package com.example.restgarden.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.GraveHorizontalViewHolder
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.viewmodel.GraveViewModel

class GraveHorizontalAdapter(
  private val graveList: List<Grave>,
  private val graveViewModel: GraveViewModel
) :
  RecyclerView.Adapter<GraveHorizontalViewHolder>(), Filterable {
  
  var graveFilterList = ArrayList<Grave>()
  
  init {
    graveFilterList = graveList as ArrayList<Grave>
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraveHorizontalViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_grave_horizontal, parent, false)
    return GraveHorizontalViewHolder(view, graveViewModel)
  }
  
  override fun onBindViewHolder(holderHorizontal: GraveHorizontalViewHolder, position: Int) {
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
