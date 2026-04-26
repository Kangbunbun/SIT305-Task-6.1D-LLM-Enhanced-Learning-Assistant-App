package com.example.llm_enhancedlearningassistantapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.llm_enhancedlearningassistantapp.R
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentSignUpBinding
import com.example.llm_enhancedlearningassistantapp.ui.BaseFragment
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateAccount.setOnClickListener {
            if (validateInput()) {
                findNavController().navigate(R.id.action_signUpFragment_to_interestsFragment)
            }
        }
    }

    private fun validateInput(): Boolean {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val confirmEmail = binding.etConfirmEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email != confirmEmail) {
            Toast.makeText(requireContext(), "Emails do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
