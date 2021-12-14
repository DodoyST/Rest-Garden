package com.example.restgarden.ui.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserUpdateRequest
import com.example.restgarden.data.repository.UserRepository
import com.example.restgarden.data.viewmodel.UserViewModel
import com.example.restgarden.databinding.FragmentUserFormBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserFormFragment : DaggerFragment() {
  private var _binding: FragmentUserFormBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var userViewModel: UserViewModel
  
  @Inject
  lateinit var userRepository: UserRepository
  
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
    _binding = FragmentUserFormBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    instanceViewModel()
    
    binding.apply {
      btnUserFormSave.setOnClickListener {
        alertUpdate()
      }
    }
    
    subscribe()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun instanceViewModel() {
    userViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UserViewModel(userRepository) as T
    })[UserViewModel::class.java]
  }
  
  private fun subscribe() {
    userViewModel.user.observe(viewLifecycleOwner, {
      val user = it.data
      if (user != null) {
        binding.apply {
          etUserFormFullName.setText(user.name)
          etUserFormAddress.setText(user.address)
          etUserFormEmail.setText(user.email)
          etUserFormPhoneNumber.setText(user.phoneNumber)
        }
      }
    })
  }
  
  private fun instanceUserUpdateRequest() = sessionManager.fetchAuthId()
    ?.let {
      UserUpdateRequest(
        it,
        binding.etUserFormFullName.text.toString(),
        binding.etUserFormEmail.text.toString(),
        binding.etUserFormPhoneNumber.text.toString(),
        binding.etUserFormAddress.text.toString()
      )
    }
  
  private fun alertUpdate() {
    AlertDialog.Builder(requireContext())
      .setMessage("Are you sure you want to change your information?")
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
        update()
        dialog.dismiss()
      }.show()
  }
  
  private fun update() {
    instanceUserUpdateRequest()?.let { userUpdateRequest ->
      userViewModel.update(userUpdateRequest).observe(viewLifecycleOwner, {
        when (it) {
          is AppResource.Success -> it.data?.let { it1 -> updateSuccess(it1) }
          is AppResource.Error -> it.message?.let { it1 -> updateError(it1) }
          is AppResource.Loading -> isLoading()
        }
      })
    }
  }
  
  private fun updateSuccess(user: User) {
    userViewModel.setUser(user)
    Snackbar.make(
      requireView(),
      "Your personal data has been successfully updated",
      Snackbar.LENGTH_LONG
    ).show()
    findNavController().navigate(R.id.action_global_profileFragment)
    isNotLoading()
  }
  
  private fun updateError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      lnlUserForm.visibility = View.VISIBLE
      pbUserForm.visibility = View.GONE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      lnlUserForm.visibility = View.INVISIBLE
      pbUserForm.visibility = View.VISIBLE
    }
  }
}
