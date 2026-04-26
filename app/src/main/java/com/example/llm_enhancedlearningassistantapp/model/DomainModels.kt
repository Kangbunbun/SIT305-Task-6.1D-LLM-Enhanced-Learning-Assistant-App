package com.example.llm_enhancedlearningassistantapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// --- UI State ---
sealed class UIState<out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}

// --- AI API Models ---
data class AIPromptRequest(val prompt: String)
data class AIPromptResponse(val response: String)

// --- Learning Utility Models ---
data class HintRequest(
    val topic: String,
    val question: String,
    val options: List<String>
)

data class HintResponse(
    val prompt: String,
    val response: String
)

data class ExplainRequest(
    val topic: String,
    val question: String,
    val correctAnswer: String,
    val userAnswer: String
)

data class ExplainResponse(
    val prompt: String,
    val response: String
)

// --- Task & Learning Models ---
@Parcelize
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val topic: String,
    val questions: List<Question>
) : Parcelable

@Parcelize
data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswer: String
) : Parcelable

@Parcelize
data class TaskResult(
    val taskId: String,
    val topic: String,
    val score: Int,
    val totalQuestions: Int,
    val questionResults: List<QuestionResult>
) : Parcelable

@Parcelize
data class QuestionResult(
    val question: Question,
    val userAnswer: String,
    val isCorrect: Boolean
) : Parcelable
