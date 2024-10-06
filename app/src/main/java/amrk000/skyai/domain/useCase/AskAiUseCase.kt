package amrk000.skyai.domain.useCase

import amrk000.skyai.data.model.AiResponseData.AiResponseData
import amrk000.skyai.data.model.ResponseData
import amrk000.skyai.data.repository.AiRepository
import android.util.Log
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class AskAiUseCase @Inject constructor(val aiRepository: AiRepository) {
    suspend operator fun invoke(question: String): AiResponseData {

        var result = AiResponseData(
            null,
            null
        )

        try {
            result = aiRepository.askGeminiAi(question)
            result.code = 200
            result.message = "success"
        }
        catch (exception: Exception) {
            exception.printStackTrace()

            if (exception is HttpException) {
                result.code = exception.code()
                result.message = exception.message()

                Log.e(
                    "Http Request Error: ",
                    "Code: ${exception.code()} | Message: ${exception.message()}"
                )
            }
            else if (exception is UnknownHostException) {
                result.code = ResponseData.FAILED_REQUEST_FAILURE
                result.message = "Request Error"
            }

        }

        return result
    }
}