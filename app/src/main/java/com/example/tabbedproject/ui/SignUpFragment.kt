package com.example.tabbedproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tabbedproject.TaskApplication
import com.example.tabbedproject.data.User
import com.example.tabbedproject.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels { TaskViewModelFactory((requireActivity().application as TaskApplication).repository) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createUser.setOnClickListener {
            if (isValidated()) {
                val newUser = User(binding.username.text.toString(), binding.password.text.toString())
                sharedViewModel.insertUser(newUser)
                Toast.makeText(requireContext(), "New user created.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment2())

            } else Toast.makeText(requireContext(), "Password was not confirmed. Enter your password again.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isValidated(): Boolean {

        if (binding.password.text.toString() == binding.confirmPassword.text.toString() && binding.username.text.toString()
                .isNotBlank() && binding.password.text.toString().isNotBlank()
        ) {
            sharedViewModel.userList.forEach {
                if (it.username != binding.username.text.toString()) return true
            }
        }
        return false

    }
}