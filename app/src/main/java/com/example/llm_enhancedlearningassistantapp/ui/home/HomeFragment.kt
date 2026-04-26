package com.example.llm_enhancedlearningassistantapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llm_enhancedlearningassistantapp.data.UserPrefs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentHomeBinding
import com.example.llm_enhancedlearningassistantapp.ui.BaseFragment
import com.example.llm_enhancedlearningassistantapp.ui.LearningViewModel
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var userPrefs: UserPrefs
    private val viewModel: LearningViewModel by activityViewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPrefs(requireContext())
        binding.tvGreeting.text = "Hello, ${userPrefs.getUsername()}"

        setupRecyclerView()
        observeViewModel()

        viewModel.loadTasks(userPrefs.getInterests())
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter { task ->
            val action = HomeFragmentDirections.actionHomeFragmentToTaskFragment(task)
            findNavController().navigate(action)
        }
        binding.rvTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.recommendedTasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNotEmpty()) {
                taskAdapter.submitList(tasks)
                binding.tvTaskSummary.text = "You have ${tasks.size} tasks available based on your interests."
            } else {
                taskAdapter.submitList(emptyList())
                binding.tvTaskSummary.text = "No tasks available for your interests."
            }
        }
    }
}
