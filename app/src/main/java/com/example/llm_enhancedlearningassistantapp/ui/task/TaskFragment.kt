package com.example.llm_enhancedlearningassistantapp.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentTaskBinding
import com.example.llm_enhancedlearningassistantapp.databinding.ItemQuestionBinding
import com.example.llm_enhancedlearningassistantapp.model.QuestionResult
import com.example.llm_enhancedlearningassistantapp.model.Task
import com.example.llm_enhancedlearningassistantapp.model.TaskResult
import com.example.llm_enhancedlearningassistantapp.model.UIState
import com.example.llm_enhancedlearningassistantapp.ui.LearningViewModel

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val args: TaskFragmentArgs by navArgs()
    private val viewModel: LearningViewModel by activityViewModels()

    private val userAnswers = mutableMapOf<Int, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val task = args.task
        binding.tvTaskTitle.text = task.title
        binding.tvTaskTopic.text = "Topic: ${task.topic}"

        setupQuestions(task)
        setupSubmitButton(task)
        observeViewModel()
    }

    private fun setupSubmitButton(task: Task) {
        binding.btnSubmit.setOnClickListener {
            if (userAnswers.size == task.questions.size) {
                val results = evaluateTask(task)
                val action = TaskFragmentDirections.actionTaskFragmentToResultsFragment(results)
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "Please answer all questions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupQuestions(task: Task) {
        binding.llQuestions.removeAllViews()
        userAnswers.clear()

        task.questions.forEach { question ->
            val qBinding = ItemQuestionBinding.inflate(layoutInflater, binding.llQuestions, false)
            qBinding.tvQuestionText.text = question.text
            qBinding.rgOptions.removeAllViews()

            question.options.forEach { option ->
                val rb = RadioButton(requireContext()).apply {
                    id = View.generateViewId()
                    text = option
                    textSize = 14f
                    setOnClickListener {
                        userAnswers[question.id] = option
                    }
                }
                qBinding.rgOptions.addView(rb)
            }

            qBinding.btnGetHint.setOnClickListener {
                viewModel.getHint(question, task.topic)
            }

            qBinding.root.tag = question.id
            binding.llQuestions.addView(qBinding.root)
        }
    }

    private fun observeViewModel() {
        viewModel.hintStates.observe(viewLifecycleOwner) { states ->
            states.forEach { (questionId, state) ->
                val qView = binding.llQuestions.findViewWithTag<View>(questionId) ?: return@forEach
                val qBinding = ItemQuestionBinding.bind(qView)

                when (state) {
                    is UIState.Loading -> {
                        binding.loadingOverlay.visibility = View.VISIBLE
                    }

                    is UIState.Success -> {
                        binding.loadingOverlay.visibility = View.GONE
                        qBinding.llHintContainer.visibility = View.VISIBLE
                        qBinding.tvHintPrompt.text = "PROMPT SENT:\n${state.data.prompt}"
                        qBinding.tvHintResponse.text = "AI RESPONSE:\n${state.data.response}"
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

    private fun evaluateTask(task: Task): TaskResult {
        var score = 0

        val questionResults = task.questions.map { question ->
            val userAnswer = userAnswers[question.id] ?: ""
            val isCorrect = userAnswer == question.correctAnswer

            if (isCorrect) {
                score++
            }

            QuestionResult(
                question = question,
                userAnswer = userAnswer,
                isCorrect = isCorrect
            )
        }

        return TaskResult(
            taskId = task.id,
            topic = task.topic,
            score = score,
            totalQuestions = task.questions.size,
            questionResults = questionResults
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
