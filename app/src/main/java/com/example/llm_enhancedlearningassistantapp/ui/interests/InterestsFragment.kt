package com.example.llm_enhancedlearningassistantapp.ui.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.llm_enhancedlearningassistantapp.R
import com.example.llm_enhancedlearningassistantapp.data.UserPrefs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentInterestsBinding
import com.google.android.material.chip.Chip

class InterestsFragment : Fragment() {

    private var _binding: FragmentInterestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPrefs: UserPrefs

    private val topics = listOf(
        "Algorithms", "Data Structures", "Web Development",
        "Testing", "Databases", "Mobile Development", "UI/UX", "Networking"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterestsBinding.inflate(inflater, container, false)
        userPrefs = UserPrefs(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topics.forEach { topic ->
            val chip = Chip(requireContext()).apply {
                text = topic
                isCheckable = true
                setChipBackgroundColorResource(R.color.extra_light_gray)
                setTextColor(resources.getColor(R.color.black, null))
            }
            binding.chipGroupInterests.addView(chip)
        }

        binding.btnNext.setOnClickListener {
            val selectedInterests = mutableSetOf<String>()
            for (i in 0 until binding.chipGroupInterests.childCount) {
                val chip = binding.chipGroupInterests.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedInterests.add(chip.text.toString())
                }
            }
            userPrefs.saveInterests(selectedInterests)
            findNavController().navigate(R.id.action_interestsFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
