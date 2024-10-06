package amrk000.skyai.presentation.viewModel

import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.data.model.WeatherData.Daily
import amrk000.skyai.data.model.WeatherData.Hourly
import amrk000.skyai.presentation.service.WeatherService
import amrk000.skyai.domain.util.SharedPrefManager
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import amrk000.skyai.data.repository.WeatherRepository
import amrk000.skyai.domain.entity.UnitsSystem
import amrk000.skyai.domain.useCase.GetUserLocationUseCase
import amrk000.skyai.domain.useCase.GetWeatherDataUseCase
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import java.time.Instant
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    application: Application,
    val getWeatherData: GetWeatherDataUseCase,
    val SharedPrefManager: SharedPrefManager
    ) : AndroidViewModel(application) {

    private var cachedWeatherData = SharedPrefManager.weatherCachedData

    private var location = SharedPrefManager.userLocation
    private var unitsSystem = SharedPrefManager.unitsSystem
    private var notification = SharedPrefManager.sendNotification

    private val weatherLiveData = MutableLiveData<WeatherData>()

    private val SERVICE_REPEAT_INTERVAL_HOURS = 12L

    fun initBackgroundService(){

        //Initialize Background Service
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            WeatherService::class.java,
            SERVICE_REPEAT_INTERVAL_HOURS,
            TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .setInputData(
                Data.Builder()
                    .putString(WeatherService.LOCATION_DATA_KEY, location?.lat + "," + location?.lng)
                    .putString(WeatherService.UNITS_DATA_KEY, unitsSystem)
                    .build()
            )
            .setInitialDelay(1, TimeUnit.HOURS)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 2, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork("notificationService", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    fun isFirstLaunch(): Boolean{
        return SharedPrefManager.firstLaunch
    }

    fun getLocationData(): UserLocation? {
        return location
    }

    fun setLocationData(location: UserLocation){
        this.location = location
        SharedPrefManager.userLocation = location
    }

    fun getCachedWeatherData(): CachedWeatherData? {
        return cachedWeatherData
    }

    fun getThisWeekForecast(): List<Daily>? {
        return cachedWeatherData?.weatherData?.timelines?.daily
    }

    fun getTodayForecast(): List<Hourly>?{
        return cachedWeatherData?.weatherData?.timelines?.hourly?.filter {
            val givenDate = Instant.parse(it.time).atZone(TimeZone.getDefault().toZoneId()).toLocalDate()
            val todayDate = Instant.now().atZone(TimeZone.getDefault().toZoneId()).toLocalDate()
            givenDate.compareTo(todayDate) == 0
        }
    }

    fun setCachedWeatherData(cachedWeatherData: CachedWeatherData){
        this.cachedWeatherData = cachedWeatherData
        SharedPrefManager.weatherCachedData = cachedWeatherData
    }

    fun getUnits(): UnitsSystem {
        return UnitsSystem(getApplication(), unitsSystem!!)
    }

    fun setUnitsSystem(unitsSystem: String){
        this.unitsSystem = unitsSystem
        SharedPrefManager.unitsSystem = unitsSystem
    }

    fun isNotificationEnabled(): Boolean {
        return notification
    }

    fun enableNotification(notification: Boolean) {
        this.notification = notification
        SharedPrefManager.sendNotification = notification
    }

    fun emitWeatherData(weatherData: WeatherData){
        weatherLiveData.value = weatherData
    }

    fun getWeatherDataObserver(): LiveData<WeatherData> {
        return weatherLiveData
    }

    fun getWeatherData(){
        val latLng = location?.lat + "," + location?.lng

        viewModelScope.launch {

            val result = getWeatherData(latLng, unitsSystem!!)

            if(result!=null) {
                emitWeatherData(result)

                val currentDateTime = Instant.now().toString()
                setCachedWeatherData(
                    CachedWeatherData(
                        result,
                        currentDateTime
                    )
                )
            }

        }
    }

}
