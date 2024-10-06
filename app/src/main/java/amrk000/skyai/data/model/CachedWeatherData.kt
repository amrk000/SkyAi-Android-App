package amrk000.skyai.data.model

import amrk000.skyai.data.model.WeatherData.WeatherData
import com.google.gson.annotations.SerializedName

data class CachedWeatherData(
    @SerializedName("weatherData")
    val weatherData: WeatherData,
    @SerializedName("time")
    val time: String
)
