package amrk000.skyai.presentation.service

import amrk000.skyai.R
import amrk000.skyai.data.model.CachedWeatherData

import amrk000.skyai.domain.util.DateTimeHelper
import amrk000.skyai.domain.util.SharedPrefManager
import amrk000.skyai.domain.util.WeatherCodesHelper

import amrk000.skyai.presentation.view.activity.MainActivity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.graphics.drawable.Icon
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import amrk000.skyai.data.repository.WeatherRepository
import amrk000.skyai.domain.useCase.GetWeatherDataUseCase

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.time.Instant
import javax.inject.Inject

@HiltWorker
class WeatherService @AssistedInject constructor(@Assisted val context: Context, @Assisted workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    @Inject lateinit var SharedPrefManager: SharedPrefManager
    @Inject lateinit var getWeatherData: GetWeatherDataUseCase

    private val channel = NotificationChannel("1", "Weather Notification", NotificationManager.IMPORTANCE_HIGH)
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    private val location = inputData.getString(LOCATION_DATA_KEY)
    private val unitsSystem = inputData.getString(UNITS_DATA_KEY)

    private val unites = amrk000.skyai.domain.entity.UnitsSystem(context, unitsSystem!!)

    private val WEATHER_NOTIF_ID = 1
    private val WEATHER_NOTIF_CLICK_CODE = 0

    companion object {
        val LOCATION_DATA_KEY = "location"
        val UNITS_DATA_KEY = "units"
    }

    override suspend fun doWork(): Result {

        try {

            val weatherData = getWeatherData(location!!, unitsSystem!!)
            if(weatherData!=null) SharedPrefManager.weatherCachedData = CachedWeatherData(weatherData, Instant.now().toString())

            if(SharedPrefManager.sendNotification) {
                val timeLines = weatherData?.timelines
                val hourlyForecast = timeLines?.hourly
                val dailyForecast = timeLines?.daily

                val thisTimeWeather = hourlyForecast?.first()
                val todayWeather = dailyForecast?.first()

                val temperature = Math.round(thisTimeWeather?.values?.temperature ?: 0.0).toString()
                val statusCode = thisTimeWeather?.values?.weatherCode!!
                val feelsLike = Math.round(todayWeather?.values?.temperatureApparentAvg ?: 0.0).toString()
                val sunrise = todayWeather?.values?.sunriseTime
                val sunset = todayWeather?.values?.sunsetTime
                val isItNight = DateTimeHelper.isItNightNow(sunrise!!, sunset!!)
                var status = WeatherCodesHelper.getStatus(context, statusCode)
                var statusIcon = WeatherCodesHelper.getIcon(context, statusCode, isItNight)

                notificationManager.createNotificationChannel(channel)

                val notif = Notification.Builder(context, channel.id)

                notif.setSmallIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setLargeIcon(Icon.createWithResource(context, statusIcon))
                    .setContentTitle(context.getString(
                            R.string.its_temp_today,
                            temperature,
                            unites.temperatureUnit
                        ))
                    .setSubText(context.getString(R.string.weather_forecast))
                    .setContentText(context.getString(R.string.feels_like_c, status, feelsLike))
                    .setBadgeIconType(R.drawable.status_cloudy_morning_icon)
                    .setShowWhen(true)
                    .setOnlyAlertOnce(true)
                    .setColorized(true)
                    .setColor(context.getColor(R.color.mainColor))
                    .setContentIntent(
                        PendingIntent.getActivity(
                            context,
                            WEATHER_NOTIF_CLICK_CODE,
                            Intent(context, MainActivity::class.java),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )

                notificationManager.notify(WEATHER_NOTIF_ID, notif.build())

                Log.e("WeatherService", "Done")
            }

        }
        catch (exception: Exception){
            exception.printStackTrace()
            Log.e("WeatherService", "Error: ${exception.message} \n" + exception.printStackTrace())
            return Result.retry()
        }

        return Result.success()
    }
}