package amrk000.skyai.domain.util

import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.entity.UnitsSystem
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor (
    val sharedPreferences: SharedPreferences,
    val gson: Gson
) {

    private val FIRST_LAUNCH = "firstLaunch"
    private val LOCATION_DATA = "locationData"
    private val WEATHER_DATA = "weatherData"

    private val SEND_NOTIFICATION = "sendNotification"
    private val UNITS_SYSTEM = "unitsSystem"

    var firstLaunch: Boolean
        get() = sharedPreferences.getBoolean(FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit().putBoolean(FIRST_LAUNCH, value).apply()

    var userLocation: UserLocation?
        get() {
            val jsonString = sharedPreferences.getString(LOCATION_DATA, null)
            return gson.fromJson(jsonString, UserLocation::class.java)
        }
        set(data) {
            val json = Gson().toJson(data)
            sharedPreferences.edit().putString(LOCATION_DATA, json).apply()
        }

    var weatherCachedData: CachedWeatherData?
        get() {
            val jsonString = sharedPreferences.getString(WEATHER_DATA, null) ?: return null
            return gson.fromJson(jsonString, CachedWeatherData::class.java)
        }
        set(value) {
            val json = gson.toJson(value)
            sharedPreferences.edit().putString(WEATHER_DATA, json).apply()
        }

    var sendNotification
        get() = sharedPreferences.getBoolean(SEND_NOTIFICATION, true)
        set(value) = sharedPreferences.edit().putBoolean(SEND_NOTIFICATION, value).apply()

    var unitsSystem
        get() = sharedPreferences.getString(UNITS_SYSTEM, UnitsSystem.SYSTEM_TYPE_METRIC)
        set(value) = sharedPreferences.edit().putString(UNITS_SYSTEM, value).apply()

}