package com.example.llm_enhancedlearningassistantapp.repository

import com.example.llm_enhancedlearningassistantapp.model.AIPromptRequest
import com.example.llm_enhancedlearningassistantapp.model.ExplainRequest
import com.example.llm_enhancedlearningassistantapp.model.ExplainResponse
import com.example.llm_enhancedlearningassistantapp.model.HintRequest
import com.example.llm_enhancedlearningassistantapp.model.HintResponse
import com.example.llm_enhancedlearningassistantapp.model.Question
import com.example.llm_enhancedlearningassistantapp.model.Task
import com.example.llm_enhancedlearningassistantapp.model.UIState
import com.example.llm_enhancedlearningassistantapp.network.RetrofitClient
import kotlinx.coroutines.delay

class LearningRepository {

    private val aiService = RetrofitClient.aiService

    companion object {
        private const val USE_REAL_API = true
    }

    suspend fun getHint(request: HintRequest): UIState<HintResponse> {
        if (request.topic.isBlank()) {
            return UIState.Error("Topic is missing. Please choose a valid learning topic.")
        }
        if (request.question.isBlank()) {
            return UIState.Error("Question is missing. Cannot generate a hint.")
        }
        if (request.options.isEmpty()) {
            return UIState.Error("Answer options are missing. Cannot generate a hint.")
        }

        val fullPrompt = buildHintPrompt(request)

        return try {
            if (USE_REAL_API) {
                val apiResponse = aiService.getHint(AIPromptRequest(fullPrompt))
                val aiText = apiResponse.response.trim()

                if (aiText.isBlank()) {
                    UIState.Error("AI returned an empty hint. Please try again.")
                } else {
                    UIState.Success(HintResponse(prompt = fullPrompt, response = aiText))
                }
            } else {
                delay(1000)
                val mockText = generateMockHint(request)
                UIState.Success(HintResponse(prompt = fullPrompt, response = mockText))
            }
        } catch (e: Exception) {
            UIState.Error("Failed to generate hint: ${e.message ?: "unknown network error"}")
        }
    }

    suspend fun getExplanation(request: ExplainRequest): UIState<ExplainResponse> {
        if (request.topic.isBlank() || request.question.isBlank()) {
            return UIState.Error("Missing context for explanation.")
        }

        if (request.correctAnswer.isBlank() || request.userAnswer.isBlank()) {
            return UIState.Error("Missing answer information for explanation.")
        }

        val taskInstruction = if (request.userAnswer == request.correctAnswer) {
            """
        The student's answer is correct.
        Explain why this answer is correct.
        Do not say the student's answer is incorrect.
        Keep the explanation simple and under 80 words.
        """.trimIndent()
        } else {
            """
        The student's answer is incorrect.
        Explain why the correct answer is correct.
        Explain why the student's selected answer is incorrect.
        Keep the explanation simple and under 80 words.
        """.trimIndent()
        }

        val fullPrompt = """
        You are a tutor helping a beginner student review a quiz answer.

        Topic: ${request.topic}
        Question: ${request.question}
        Correct answer: ${request.correctAnswer}
        Student answer: ${request.userAnswer}

        Task:
        $taskInstruction
    """.trimIndent()

        return try {
            if (USE_REAL_API) {
                val apiRes = aiService.getExplanation(AIPromptRequest(fullPrompt))

                if (apiRes.response.isBlank()) {
                    UIState.Error("AI returned an empty explanation.")
                } else {
                    UIState.Success(ExplainResponse(fullPrompt, apiRes.response))
                }
            } else {
                delay(1000)

                val mockResponse = if (request.userAnswer == request.correctAnswer) {
                    "The answer '${request.correctAnswer}' is correct because it matches the key concept in ${request.topic}."
                } else {
                    "The correct answer is '${request.correctAnswer}'. The student's answer '${request.userAnswer}' does not match the concept being tested in ${request.topic}."
                }

                UIState.Success(ExplainResponse(fullPrompt, mockResponse))
            }
        } catch (e: Exception) {
            UIState.Error("Connection Failed: ${e.message}")
        }
    }

    private fun buildHintPrompt(request: HintRequest): String {
        return """
            You are a learning assistant for beginner students.

            Topic: ${request.topic}
            Question: ${request.question}
            Options: ${request.options.joinToString(", ")}

            Task:
            Give one short beginner-friendly hint.
            Do not reveal the correct answer directly.
            Keep the hint under 40 words.
        """.trimIndent()
    }

    private fun buildExplainPrompt(request: ExplainRequest): String {
        return """
            You are a tutor helping a beginner student review a quiz answer.

            Topic: ${request.topic}
            Question: ${request.question}
            Correct answer: ${request.correctAnswer}
            Student answer: ${request.userAnswer}

            Task:
            Explain why the correct answer is correct.
            Explain why the student's selected answer is incorrect if it is wrong.
            Keep the explanation simple and under 80 words.
        """.trimIndent()
    }

    private fun generateMockHint(request: HintRequest): String {
        val q = request.question.lowercase()

        return when {
            q.contains("html") ->
                "Think about the language that gives a web page its basic structure."
            q.contains("css") ->
                "Think about the part of web development that controls colours, layout, and appearance."
            q.contains("algorithm") || q.contains("o(1)") ->
                "Focus on how many steps are needed as the input gets bigger."
            q.contains("loop") ->
                "Look for the option related to repeating an action."
            q.contains("variable") ->
                "Think about a named container that stores a value."
            q.contains("database") || q.contains("table") ->
                "Think about how organized data is stored in rows and columns."
            q.contains("sql") ->
                "Think about the language used to work with database tables."
            q.contains("android") || q.contains("google play") ->
                "Think about the official marketplace used by Android users."
            q.contains("design") || q.contains("ui") ->
                "Think about making the app easier and clearer for users."
            q.contains("internet") || q.contains("network") ->
                "Think about the system that connects computers and devices together."
            else ->
                "Focus on the main purpose of this ${request.topic} concept before choosing an answer."
        }
    }

    private fun generateMockExplanation(request: ExplainRequest): String {
        return if (request.userAnswer == request.correctAnswer) {
            "Correct. '${request.correctAnswer}' fits this ${request.topic} question because it directly matches the concept being tested."
        } else {
            "The correct answer is '${request.correctAnswer}' because it best matches the ${request.topic} concept. '${request.userAnswer}' may look related, but it does not answer this question accurately."
        }
    }

    private val tasks = listOf(
        Task(
            "1",
            "Algorithms Basics",
            "Learn what an algorithm is and how simple steps solve problems.",
            "Algorithms",
            listOf(
                Question(
                    1,
                    "What is an algorithm?",
                    listOf("A set of instructions", "A type of computer", "A musical instrument"),
                    "A set of instructions"
                ),
                Question(
                    2,
                    "Which one is used to repeat a task?",
                    listOf("Loop", "Variable", "Comment"),
                    "Loop"
                )
            )
        ),
        Task(
            "2",
            "Simple Data Structures",
            "Introduction to storing and organizing data.",
            "Data Structures",
            listOf(
                Question(
                    3,
                    "Which one stores a list of items?",
                    listOf("Array", "Single Number", "Blank Page"),
                    "Array"
                ),
                Question(
                    4,
                    "What is a variable used for?",
                    listOf("Storing data", "Printing paper", "Connecting to Wi-Fi"),
                    "Storing data"
                )
            )
        ),
        Task(
            "3",
            "Web for Beginners",
            "Learn the basic building blocks of websites.",
            "Web Development",
            listOf(
                Question(
                    5,
                    "What language is used for web structure?",
                    listOf("HTML", "Python", "C++"),
                    "HTML"
                ),
                Question(
                    6,
                    "What makes a website look styled and visually clear?",
                    listOf("CSS", "SQL", "Java"),
                    "CSS"
                )
            )
        ),
        Task(
            "4",
            "Intro to Testing",
            "Understand why developers test software.",
            "Testing",
            listOf(
                Question(
                    7,
                    "Why do we test software?",
                    listOf("To find bugs", "To make it more expensive", "To change the icon"),
                    "To find bugs"
                ),
                Question(
                    8,
                    "What does a failed test usually show?",
                    listOf("Something may be wrong", "The app is always perfect", "The code needs no review"),
                    "Something may be wrong"
                )
            )
        ),
        Task(
            "5",
            "Database Basics",
            "Learn how apps store and manage data.",
            "Databases",
            listOf(
                Question(
                    9,
                    "Where is data commonly stored in a relational database?",
                    listOf("Tables", "Images only", "Emails only"),
                    "Tables"
                ),
                Question(
                    10,
                    "What is SQL used for?",
                    listOf("Managing data", "Editing photos", "Browsing the web"),
                    "Managing data"
                )
            )
        ),
        Task(
            "6",
            "Mobile Apps",
            "Understand simple mobile app concepts.",
            "Mobile Development",
            listOf(
                Question(
                    11,
                    "Where do Android users commonly download apps?",
                    listOf("Google Play Store", "Post Office", "Text Editor"),
                    "Google Play Store"
                ),
                Question(
                    12,
                    "What is an Activity in Android?",
                    listOf("A screen or entry point", "A database table", "A CSS file"),
                    "A screen or entry point"
                )
            )
        ),
        Task(
            "7",
            "Design Basics",
            "Learn simple UI and UX concepts.",
            "UI/UX",
            listOf(
                Question(
                    13,
                    "What is a main goal of good UI/UX design?",
                    listOf("Easy to use", "Tiny unreadable text", "Random buttons"),
                    "Easy to use"
                ),
                Question(
                    14,
                    "Why should button labels be clear?",
                    listOf("So users know what they do", "To confuse users", "To hide actions"),
                    "So users know what they do"
                )
            )
        ),
        Task(
            "8",
            "Internet Basics",
            "Learn how devices connect and communicate.",
            "Networking",
            listOf(
                Question(
                    15,
                    "What allows computers to communicate with each other?",
                    listOf("The Internet", "A Mouse", "A Printer"),
                    "The Internet"
                ),
                Question(
                    16,
                    "What does a network connect?",
                    listOf("Devices", "Only chairs", "Only paper files"),
                    "Devices"
                )
            )
        )
    )

    fun getTasksForInterests(interests: Set<String>): List<Task> {
        return if (interests.isEmpty()) {
            tasks
        } else {
            tasks.filter { interests.contains(it.topic) }
        }
    }
}
