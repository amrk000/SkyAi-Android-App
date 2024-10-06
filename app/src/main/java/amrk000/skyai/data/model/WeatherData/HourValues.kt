package amrk000.skyai.data.model.WeatherData


import com.google.gson.annotations.SerializedName

data class HourValues(
    @SerializedName("cloudBase")
    var cloudBase: Double?,
    @SerializedName("cloudCeiling")
    var cloudCeiling: Any?,
    @SerializedName("cloudCover")
    var cloudCover: Double,
    @SerializedName("dewPoint")
    var dewPoint: Double,
    @SerializedName("evapotranspiration")
    var evapotranspiration: Double,
    @SerializedName("freezingRainIntensity")
    var freezingRainIntensity: Double,
    @SerializedName("humidity")
    var humidity: Double,
    @SerializedName("iceAccumulation")
    var iceAccumulation: Double,
    @SerializedName("iceAccumulationLwe")
    var iceAccumulationLwe: Double?,
    @SerializedName("precipitationProbability")
    var precipitationProbability: Double,
    @SerializedName("pressureSurfaceLevel")
    var pressureSurfaceLevel: Double,
    @SerializedName("rainAccumulation")
    var rainAccumulation: Double,
    @SerializedName("rainAccumulationLwe")
    var rainAccumulationLwe: Double?,
    @SerializedName("rainIntensity")
    var rainIntensity: Double,
    @SerializedName("sleetAccumulation")
    var sleetAccumulation: Double,
    @SerializedName("sleetAccumulationLwe")
    var sleetAccumulationLwe: Double?,
    @SerializedName("sleetIntensity")
    var sleetIntensity: Double,
    @SerializedName("snowAccumulation")
    var snowAccumulation: Double,
    @SerializedName("snowAccumulationLwe")
    var snowAccumulationLwe: Double?,
    @SerializedName("snowDepth")
    var snowDepth: Double?,
    @SerializedName("snowIntensity")
    var snowIntensity: Double,
    @SerializedName("temperature")
    var temperature: Double,
    @SerializedName("temperatureApparent")
    var temperatureApparent: Double,
    @SerializedName("uvHealthConcern")
    var uvHealthConcern: Double?,
    @SerializedName("uvIndex")
    var uvIndex: Double?,
    @SerializedName("visibility")
    var visibility: Double,
    @SerializedName("weatherCode")
    var weatherCode: Int,
    @SerializedName("windDirection")
    var windDirection: Double,
    @SerializedName("windGust")
    var windGust: Double,
    @SerializedName("windSpeed")
    var windSpeed: Double
)