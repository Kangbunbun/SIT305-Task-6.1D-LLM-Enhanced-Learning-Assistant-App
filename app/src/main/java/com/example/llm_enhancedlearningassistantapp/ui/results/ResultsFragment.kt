package com.example.llm_enhancedlearningassistantapp.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.llm_enhancedlearningassistantapp.R
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentResultsBinding
import com.example.llm_enhancedlearningassistantapp.databinding.ItemResultBinding
import com.example.llm_enhancedlearningassistantapp.model.TaskResult
import com.example.llm_enhancedlearningassistantapp.model.UIState
import com.example.llm_enhancedlearningassistantapp.ui.LearningViewModel

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private val args: ResultsFragmentArgs by navArgs()
    private val viewModel: LearningViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = args.result
        binding.tvScore.text = "Score: ${result.score}/${result.totalQuestions}"

        setupResults(result)
        setupDoneButton()
        observeViewModel()
    }

    private fun setupResults(result: TaskResult) {
        binding.llResults.removeAllViews()

        result.questionResults.forEach { qResult ->
            val rBinding = ItemResultBinding.inflate(layoutInflater, binding.llResults, false)

            rBinding.tvQuestionText.text = qResult.question.text
            rBinding.tvUserAnswer.text = "Your answer: ${qResult.userAnswer}"
            rBinding.tvCorrectAnswer.text = "Correct answer: ${qResult.question.correctAnswer}"

            if (qResult.isCorrect) {
                rBinding.tvStatus.text = "CORRECT"
                rBinding.tvStatus.setBackgroundColor(resources.getColor(R.color.light_gray, null))
            } else {
                rBinding.tvStatus.text = "INCORRECT"
                rBinding.tvStatus.setBackgroundColor(resources.getColor(R.color.extra_light_gray, null))
            }

            rBinding.btnExplain.setOnClickListener {
                viewModel.getExplanation(
                    question = qResult.question,
                    userAnswer = qResult.userAnswer,
                    topic = result.topic
                )
            }

            rBinding.root.tag = qResult.question.id
            binding.llResults.addView(rBinding.root)
        }
    }

    private fun setupDoneButton() {
        binding.btnDone.setOnClickListener {
            viewModel.clearAIStates()
            findNavController().navigate(R.id.action_resultsFragment_to_homeFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.explainStates.observe(viewLifecycleOwner) { states ->
            states.forEach { (questionId, state) ->
                val rView = binding.llResults.findViewWithTag<View>(questionId) ?: return@forEach
                val rBinding = ItemResultBinding.bind(rView)

                when (state) {
                    is UIState.Loading -> {
                        binding.loadingOverlay.visibility = View.VISIBLE
                    }

                    is UIState.Success -> {
                        binding.loadingOverlay.visibility = View.GONE
                        rBinding.llExplanationContainer.visibility = View.VISIBLE
                        rBinding.tvExplainPrompt.text = "PROMPT SENT:\n${state.data.prompt}"
                        rBinding.tvExplainResponse.text = "AI EXPLANATION:\n${state.data.response}"
                    }

                    is UIState.Error -> {
                        binding.loadingOverlay.visibility = View.GONE
                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
