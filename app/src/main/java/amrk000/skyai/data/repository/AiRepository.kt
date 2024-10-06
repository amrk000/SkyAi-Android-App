package amrk000.skyai.data.repository

import amrk000.skyai.BuildConfig
import amrk000.skyai.data.model.AiRequestData.AiRequestData
import amrk000.skyai.data.model.AiRequestData.Content
import amrk000.skyai.data.model.AiRequestData.Part
import amrk000.skyai.data.model.AiResponseData.AiResponseData
import amrk000.skyai.data.remote.geminiAiAPI.GeminiAiClient
import amrk000.skyai.domain.repository.IAiRepository
import javax.inject.Inject

class AiRepository @Inject constructor() : IAiRepository {
    override val API_KEY = BuildConfig.AI_API_KEY
    @Inject lateinit var client: GeminiAiClient

    override suspend fun askGeminiAi(data: String): AiResponseData {
        val requestBody = AiRequestData(
            listOf(
                Content(
                    listOf(
                        Part(data)
                    )
                )
            )
        )

        return client.getService.askGeminiAi(API_KEY, requestBody)
    }
}