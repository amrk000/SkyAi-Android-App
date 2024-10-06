package amrk000.skyai.data.model.WeatherData


import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("time")
    var time: String,
    @SerializedName("values")
    var values: DayValues
)