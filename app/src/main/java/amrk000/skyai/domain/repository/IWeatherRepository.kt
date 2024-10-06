package amrk000.skyai.domain.repository

import amrk000.skyai.BuildConfig
import amrk000.skyai.data.remote.weatherAPI.WeatherClient
import amrk000.skyai.data.model.WeatherData.WeatherData
import javax.inject.Inject

interface IWeatherRepository{
    val WEATHER_API_KEY: String

    suspend fun getWeatherData(location: String, units: String): WeatherData
}