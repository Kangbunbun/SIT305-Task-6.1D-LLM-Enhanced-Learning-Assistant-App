package com.example.llm_enhancedlearningassistantapp.network

import com.example.llm_enhancedlearningassistantapp.model.AIPromptRequest
import com.example.llm_enhancedlearningassistantapp.model.AIPromptResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AIService {
    @POST("ai/hint")
    suspend fun getHint(@Body request: AIPromptRequest): AIPromptResponse

    @POST("ai/explain")
    suspend fun getExplanation(@Body request: AIPromptRequest): AIPromptResponse
}
