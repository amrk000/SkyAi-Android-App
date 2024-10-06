package amrk000.skyai.data.repository

import amrk000.skyai.BuildConfig
import amrk000.skyai.data.remote.weatherAPI.WeatherClient
import amrk000.skyai.domain.repository.IWeatherRepository
import javax.inject.Inject

class WeatherRepository @Inject constructor(): IWeatherRepository {
    override val WEATHER_API_KEY = BuildConfig.WEATHER_API_KEY
    @Inject lateinit var client: WeatherClient

    override suspend fun getWeatherData(location: String, units: String): amrk000.skyai.data.model.WeatherData.WeatherData {
        return client.getService.getWeatherData(WEATHER_API_KEY, location, "current", units, "1h,1d")
    }

}