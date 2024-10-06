package amrk000.skyai.data.model.WeatherData


import amrk000.skyai.data.model.ResponseData
import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("timelines")
    var timelines: Timelines?
) : ResponseData()