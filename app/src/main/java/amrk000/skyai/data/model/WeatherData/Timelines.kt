package amrk000.skyai.data.model.WeatherData


import com.google.gson.annotations.SerializedName

data class Timelines(
    @SerializedName("daily")
    var daily: ArrayList<Daily>,
    @SerializedName("hourly")
    var hourly: ArrayList<Hourly>
)