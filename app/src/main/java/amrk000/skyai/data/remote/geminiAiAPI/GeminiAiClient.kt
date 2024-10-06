package amrk000.skyai.data.remote.geminiAiAPI

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiAiClient @Inject constructor() {
    @Inject
    lateinit var getService: IGeminiAiApiService
}