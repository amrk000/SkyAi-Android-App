package amrk000.skyai.data.remote.weatherAPI

import amrk000.skyai.data.model.WeatherData.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApiService {
    @GET("forecast")
    suspend fun getWeatherData(
        @Query("apikey") apiKey: String,
        @Query("location") location: String,
        @Query("timestamp") timeStamp: String,
        @Query("units") units: String,
        @Query("timesteps") timeSteps: String,
    ): WeatherData

}