package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restgarden.R
import com.example.restgarden.data.adapter.GraveHorizontalAdapter
import com.example.restgarden.data.adapter.GraveVerticalAdapter
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.databinding.FragmentHomeBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.google.android.material.snackbar.Snackbar
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
  
  @Inject
  lateinit var sessionManager: SessionManager
  
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
      btnGraveList.setOnClickListener {
        if (!sessionManager.isLoggedIn()) findNavController().navigate(R.id.action_global_authActivity)
      }
      
      etGraveListSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
          graveHorizontalAdapter.filter.filter(newText)
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
        is AppResource.Success -> it.data?.let { it1 -> subscribeSuccess(it1) }
        is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun subscribeSuccess(graveList: List<Grave>) {
    graveHorizontalAdapter =
      GraveHorizontalAdapter(
        graveList.filter { it.type == "Private" && it.availableSlots > 0 },
        graveViewModel
      )
    graveVerticalAdapter =
      GraveVerticalAdapter(
        graveList.filter { it.type == "Public" && it.availableSlots > 0 },
        graveViewModel
      )
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
  
  private fun subscribeError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
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
