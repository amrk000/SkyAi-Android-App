package amrk000.skyai.data.model.WeatherData


import com.google.gson.annotations.SerializedName

data class DayValues(
    @SerializedName("cloudBaseAvg")
    var cloudBaseAvg: Double,
    @SerializedName("cloudBaseMax")
    var cloudBaseMax: Double,
    @SerializedName("cloudBaseMin")
    var cloudBaseMin: Double,
    @SerializedName("cloudCeilingAvg")
    var cloudCeilingAvg: Double,
    @SerializedName("cloudCeilingMax")
    var cloudCeilingMax: Double,
    @SerializedName("cloudCeilingMin")
    var cloudCeilingMin: Double,
    @SerializedName("cloudCoverAvg")
    var cloudCoverAvg: Double,
    @SerializedName("cloudCoverMax")
    var cloudCoverMax: Double,
    @SerializedName("cloudCoverMin")
    var cloudCoverMin: Double,
    @SerializedName("dewPointAvg")
    var dewPointAvg: Double,
    @SerializedName("dewPointMax")
    var dewPointMax: Double,
    @SerializedName("dewPointMin")
    var dewPointMin: Double,
    @SerializedName("evapotranspirationAvg")
    var evapotranspirationAvg: Double,
    @SerializedName("evapotranspirationMax")
    var evapotranspirationMax: Double,
    @SerializedName("evapotranspirationMin")
    var evapotranspirationMin: Double,
    @SerializedName("evapotranspirationSum")
    var evapotranspirationSum: Double,
    @SerializedName("freezingRainIntensityAvg")
    var freezingRainIntensityAvg: Double,
    @SerializedName("freezingRainIntensityMax")
    var freezingRainIntensityMax: Double,
    @SerializedName("freezingRainIntensityMin")
    var freezingRainIntensityMin: Double,
    @SerializedName("humidityAvg")
    var humidityAvg: Double,
    @SerializedName("humidityMax")
    var humidityMax: Double,
    @SerializedName("humidityMin")
    var humidityMin: Double,
    @SerializedName("iceAccumulationAvg")
    var iceAccumulationAvg: Double,
    @SerializedName("iceAccumulationLweAvg")
    var iceAccumulationLweAvg: Double,
    @SerializedName("iceAccumulationLweMax")
    var iceAccumulationLweMax: Double,
    @SerializedName("iceAccumulationLweMin")
    var iceAccumulationLweMin: Double,
    @SerializedName("iceAccumulationLweSum")
    var iceAccumulationLweSum: Double,
    @SerializedName("iceAccumulationMax")
    var iceAccumulationMax: Double,
    @SerializedName("iceAccumulationMin")
    var iceAccumulationMin: Double,
    @SerializedName("iceAccumulationSum")
    var iceAccumulationSum: Double,
    @SerializedName("moonriseTime")
    var moonriseTime: String,
    @SerializedName("moonsetTime")
    var moonsetTime: String,
    @SerializedName("precipitationProbabilityAvg")
    var precipitationProbabilityAvg: Double,
    @SerializedName("precipitationProbabilityMax")
    var precipitationProbabilityMax: Double,
    @SerializedName("precipitationProbabilityMin")
    var precipitationProbabilityMin: Double,
    @SerializedName("pressureSurfaceLevelAvg")
    var pressureSurfaceLevelAvg: Double,
    @SerializedName("pressureSurfaceLevelMax")
    var pressureSurfaceLevelMax: Double,
    @SerializedName("pressureSurfaceLevelMin")
    var pressureSurfaceLevelMin: Double,
    @SerializedName("rainAccumulationAvg")
    var rainAccumulationAvg: Double,
    @SerializedName("rainAccumulationLweAvg")
    var rainAccumulationLweAvg: Double,
    @SerializedName("rainAccumulationLweMax")
    var rainAccumulationLweMax: Double,
    @SerializedName("rainAccumulationLweMin")
    var rainAccumulationLweMin: Double,
    @SerializedName("rainAccumulationMax")
    var rainAccumulationMax: Double,
    @SerializedName("rainAccumulationMin")
    var rainAccumulationMin: Double,
    @SerializedName("rainAccumulationSum")
    var rainAccumulationSum: Double,
    @SerializedName("rainIntensityAvg")
    var rainIntensityAvg: Double,
    @SerializedName("rainIntensityMax")
    var rainIntensityMax: Double,
    @SerializedName("rainIntensityMin")
    var rainIntensityMin: Double,
    @SerializedName("sleetAccumulationAvg")
    var sleetAccumulationAvg: Double,
    @SerializedName("sleetAccumulationLweAvg")
    var sleetAccumulationLweAvg: Double,
    @SerializedName("sleetAccumulationLweMax")
    var sleetAccumulationLweMax: Double,
    @SerializedName("sleetAccumulationLweMin")
    var sleetAccumulationLweMin: Double,
    @SerializedName("sleetAccumulationLweSum")
    var sleetAccumulationLweSum: Double,
    @SerializedName("sleetAccumulationMax")
    var sleetAccumulationMax: Double,
    @SerializedName("sleetAccumulationMin")
    var sleetAccumulationMin: Double,
    @SerializedName("sleetIntensityAvg")
    var sleetIntensityAvg: Double,
    @SerializedName("sleetIntensityMax")
    var sleetIntensityMax: Double,
    @SerializedName("sleetIntensityMin")
    var sleetIntensityMin: Double,
    @SerializedName("snowAccumulationAvg")
    var snowAccumulationAvg: Double,
    @SerializedName("snowAccumulationLweAvg")
    var snowAccumulationLweAvg: Double,
    @SerializedName("snowAccumulationLweMax")
    var snowAccumulationLweMax: Double,
    @SerializedName("snowAccumulationLweMin")
    var snowAccumulationLweMin: Double,
    @SerializedName("snowAccumulationLweSum")
    var snowAccumulationLweSum: Double,
    @SerializedName("snowAccumulationMax")
    var snowAccumulationMax: Double,
    @SerializedName("snowAccumulationMin")
    var snowAccumulationMin: Double,
    @SerializedName("snowAccumulationSum")
    var snowAccumulationSum: Double,
    @SerializedName("snowDepthAvg")
    var snowDepthAvg: Double?,
    @SerializedName("snowDepthMax")
    var snowDepthMax: Double?,
    @SerializedName("snowDepthMin")
    var snowDepthMin: Double?,
    @SerializedName("snowDepthSum")
    var snowDepthSum: Double?,
    @SerializedName("snowIntensityAvg")
    var snowIntensityAvg: Double,
    @SerializedName("snowIntensityMax")
    var snowIntensityMax: Double,
    @SerializedName("snowIntensityMin")
    var snowIntensityMin: Double,
    @SerializedName("sunriseTime")
    var sunriseTime: String,
    @SerializedName("sunsetTime")
    var sunsetTime: String,
    @SerializedName("temperatureApparentAvg")
    var temperatureApparentAvg: Double,
    @SerializedName("temperatureApparentMax")
    var temperatureApparentMax: Double,
    @SerializedName("temperatureApparentMin")
    var temperatureApparentMin: Double,
    @SerializedName("temperatureAvg")
    var temperatureAvg: Double,
    @SerializedName("temperatureMax")
    var temperatureMax: Double,
    @SerializedName("temperatureMin")
    var temperatureMin: Double,
    @SerializedName("uvHealthConcernAvg")
    var uvHealthConcernAvg: Double?,
    @SerializedName("uvHealthConcernMax")
    var uvHealthConcernMax: Double?,
    @SerializedName("uvHealthConcernMin")
    var uvHealthConcernMin: Double?,
    @SerializedName("uvIndexAvg")
    var uvIndexAvg: Double?,
    @SerializedName("uvIndexMax")
    var uvIndexMax: Double?,
    @SerializedName("uvIndexMin")
    var uvIndexMin: Double?,
    @SerializedName("visibilityAvg")
    var visibilityAvg: Double,
    @SerializedName("visibilityMax")
    var visibilityMax: Double,
    @SerializedName("visibilityMin")
    var visibilityMin: Double,
    @SerializedName("weatherCodeMax")
    var weatherCodeMax: Int,
    @SerializedName("weatherCodeMin")
    var weatherCodeMin: Int,
    @SerializedName("windDirectionAvg")
    var windDirectionAvg: Double,
    @SerializedName("windGustAvg")
    var windGustAvg: Double,
    @SerializedName("windGustMax")
    var windGustMax: Double,
    @SerializedName("windGustMin")
    var windGustMin: Double,
    @SerializedName("windSpeedAvg")
    var windSpeedAvg: Double,
    @SerializedName("windSpeedMax")
    var windSpeedMax: Double,
    @SerializedName("windSpeedMin")
    var windSpeedMin: Double
)