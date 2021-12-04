package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restgarden.R
import com.example.restgarden.data.adapter.GraveHorizontalAdapter
import com.example.restgarden.data.adapter.GraveVerticalAdapter
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.databinding.FragmentHomeBinding
import com.example.restgarden.util.AppResource
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {
  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var graveViewModel: GraveViewModel
  
  @Inject
  lateinit var graveRepositoryImpl: GraveRepositoryImpl
  private lateinit var graveHorizontalAdapter: GraveHorizontalAdapter
  private lateinit var graveVerticalAdapter: GraveVerticalAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    graveViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GraveViewModel(graveRepositoryImpl) as T
    })[GraveViewModel::class.java]
    
    subscribe()
    
    binding.apply {
      etGraveListSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
          graveVerticalAdapter.filter.filter(newText)
          return true
        }
      })
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun subscribe() {
    graveViewModel.getAll().observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> {
          if (it.data != null) {
            val response = it.data
            graveHorizontalAdapter = if (response.size > 3) {
              val randomGraves = response.asSequence().shuffled().take(3).toList()
              GraveHorizontalAdapter(randomGraves, graveViewModel)
            } else GraveHorizontalAdapter(response, graveViewModel)
            graveVerticalAdapter = GraveVerticalAdapter(response, graveViewModel)
            binding.apply {
              rvCardGraveHorizontal.apply {
                adapter = graveHorizontalAdapter
                layoutManager =
                  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
              }
              rvCardGraveVertical.apply {
                adapter = graveVerticalAdapter
                layoutManager =
                  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
              }
            }
            isNotLoading()
          }
        }
        is AppResource.Error -> {
          Toast.makeText(requireContext(), "Something Wrong...", Toast.LENGTH_LONG).show()
        }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun isNotLoading() {
    binding.apply {
      svHoveGrave.visibility = View.VISIBLE
      pbHome.visibility = View.GONE
      etGraveListSearch.queryHint = getString(R.string.search)
    }
  }
  
  private fun isLoading() {
    binding.apply {
      svHoveGrave.visibility = View.GONE
      pbHome.visibility = View.VISIBLE
      etGraveListSearch.queryHint = getString(R.string.please_wait)
    }
  }
}