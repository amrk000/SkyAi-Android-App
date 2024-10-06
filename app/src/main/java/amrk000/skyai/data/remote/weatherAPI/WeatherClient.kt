package amrk000.skyai.data.remote.weatherAPI

import amrk000.skyai.data.remote.geminiAiAPI.IGeminiAiApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherClient @Inject constructor() {
    @Inject
    lateinit var getService: IWeatherApiService

}