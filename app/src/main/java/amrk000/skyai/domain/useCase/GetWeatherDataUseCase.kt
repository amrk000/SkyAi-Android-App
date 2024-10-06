package amrk000.skyai.domain.useCase

import amrk000.skyai.data.model.AiResponseData.AiResponseData
import amrk000.skyai.data.model.ResponseData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.data.repository.AiRepository
import amrk000.skyai.data.repository.WeatherRepository
import android.util.Log
import retrofit2.HttpException
import java.net.UnknownHostException
import java.time.Instant
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(latLng: String, unitsSystem: String): WeatherData? {

        var result:WeatherData? = null

        try {
            val response = weatherRepository.getWeatherData(latLng, unitsSystem)

            response.code = 200
            response.message = "success"
            result = response
        }
        catch (exception: Exception){
            exception.printStackTrace()

            if (exception is HttpException) {
                result = WeatherData(null)
                    .apply {
                        code = exception.code()
                        message = exception.message()
                    }

                Log.e(
                    "Http Request Error: ",
                    "Code: ${exception.code()} | Message: ${exception.message()}"
                )
            }
            else if(exception is UnknownHostException){
                result = WeatherData(null)
                    .apply {
                        code = ResponseData.FAILED_REQUEST_FAILURE
                        message = "Request Error"
                    }
            }

        }

        return result
    }
}