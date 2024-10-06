package amrk000.skyai.domain.repository

import amrk000.skyai.BuildConfig
import amrk000.skyai.data.remote.geminiAiAPI.GeminiAiClient
import amrk000.skyai.data.model.AiResponseData.AiResponseData
import javax.inject.Inject

interface IAiRepository{
    val API_KEY: String

    suspend fun askGeminiAi(data: String): AiResponseData

}