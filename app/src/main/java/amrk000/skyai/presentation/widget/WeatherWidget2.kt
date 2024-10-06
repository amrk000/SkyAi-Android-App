package amrk000.skyai.presentation.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import amrk000.skyai.R
import amrk000.skyai.domain.entity.UnitsSystem
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.util.DateTimeHelper
import amrk000.skyai.domain.util.SharedPrefManager
import amrk000.skyai.domain.util.WeatherCodesHelper
import amrk000.skyai.presentation.view.activity.MainActivity
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.Instant
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class WeatherWidget2 : AppWidgetProvider() {
    @Inject
    lateinit var SharedPrefManager: SharedPrefManager

    companion object{
        const val ACTION_REFRESH = "refresh"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val views = RemoteViews(context.packageName, R.layout.weather_widget2)

        //Open App on Widget Click
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget2Layout, pendingIntent)

        //update widget instances views
        val cachedWeatherData = SharedPrefManager.weatherCachedData
        for (appWidgetId in appWidgetIds) {
            renderWeatherData(context, cachedWeatherData?.weatherData!!, views)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent!!.action) {
            ACTION_REFRESH -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context!!, this::class.java))
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }

    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    fun renderWeatherData(context: Context, weatherData : WeatherData, views: RemoteViews){
        val unites =
            UnitsSystem(context, SharedPrefManager.unitsSystem!!)

        val timeLines = weatherData.timelines

        if(timeLines!= null) {
            val hourlyForecast = timeLines.hourly.filter {
                val givenTime = Instant.parse(it.time).atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
                val currentTime = Instant.now().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
                Duration.between(givenTime, currentTime).toMinutes() < 45
            } as ArrayList

            val dailyForecast = timeLines.daily

            val thisTimeWeather = hourlyForecast.first()
            val todayWeather = dailyForecast.first()

            val temperature = Math.round(thisTimeWeather.values.temperature).toString()
            val statusCode = thisTimeWeather.values.weatherCode

            val sunrise = todayWeather.values.sunriseTime
            val sunset = todayWeather.values.sunsetTime

            val isItNight = DateTimeHelper.isItNightNow(sunrise, sunset)

            val status = WeatherCodesHelper.getStatus(context, statusCode)
            val statusIcon = WeatherCodesHelper.getIcon(context, statusCode, isItNight)

            views.setImageViewResource(R.id.widget2StatusIcon, statusIcon)
            views.setTextViewText(R.id.widget2Status, status)
            views.setTextViewText(R.id.widget2TempUnit, unites.temperatureUnit)
            views.setTextViewText(R.id.widget2Temp, temperature)

            }

        }
}