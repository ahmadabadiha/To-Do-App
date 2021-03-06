package com.example.tabbedproject.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tabbedproject.R
import com.example.tabbedproject.TaskApplication
import com.example.tabbedproject.data.User
import com.example.tabbedproject.databinding.FragmentLoginBinding
import com.example.tabbedproject.ui.sharedviewmodel.SharedViewModel
import com.example.tabbedproject.ui.sharedviewmodel.TaskViewModelFactory

class LoginFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels { TaskViewModelFactory((requireActivity().application as TaskApplication).repository) }
    private lateinit var binding: FragmentLoginBinding
    private lateinit var username: String
    private var loggedIn = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverSet()
        initSetClickListeners()
        handleBackButton()
        username = sharedViewModel.username


    }

    private fun initObserverSet() {
        sharedViewModel.isUserSet.observe(viewLifecycleOwner) {
            loggedIn = it
        }
    }

    private fun initSetClickListeners() {
        binding.signInButton.setOnClickListener {
            val requestedUser = User(binding.username.text.toString(), binding.password.text.toString())
            sharedViewModel.checkUserSet(requestedUser)
            if (loggedIn) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToTabFragment())
            } else Toast.makeText(requireContext(), "Requested user wasn't found. You can sign up now.", Toast.LENGTH_SHORT).show()
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToSignUpFragment())
        }
    }

    private fun handleBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.setTitle("Exit?")
                ?.setMessage("Are you sure you want to exit app?")
            builder?.setPositiveButton("Yes") { _, _ ->
                activity?.finish()
            }
                ?.setNegativeButton("No") { _, _ ->

                }?.create()?.show()

        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getUsers()
    }
}