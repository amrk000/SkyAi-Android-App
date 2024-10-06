package amrk000.skyai.data.remote.geminiAiAPI

import amrk000.skyai.data.model.AiRequestData.AiRequestData
import amrk000.skyai.data.model.AiResponseData.AiResponseData
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface IGeminiAiApiService {
    @POST("./gemini-1.5-flash-latest:generateContent")
    suspend fun askGeminiAi(
        @Query("key") apiKey: String,
        @Body requestBody: AiRequestData
    ): AiResponseData
}