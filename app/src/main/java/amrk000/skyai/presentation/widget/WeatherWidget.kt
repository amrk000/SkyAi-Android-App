package amrk000.skyai.presentation.widget

import amrk000.skyai.R
import amrk000.skyai.domain.entity.UnitsSystem
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.util.DateTimeHelper
import amrk000.skyai.domain.util.SharedPrefManager
import amrk000.skyai.domain.util.WeatherCodesHelper
import amrk000.skyai.presentation.view.activity.MainActivity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.Instant
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class WeatherWidget : AppWidgetProvider() {
    @Inject
    lateinit var SharedPrefManager: SharedPrefManager

    companion object{
        const val ACTION_REFRESH = "refresh"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val views = RemoteViews(context.packageName, R.layout.weather_widget)

        //Open App on Widget Click
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent)

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
            val windSpeed = thisTimeWeather.values.windSpeed
            val visibility = thisTimeWeather.values.visibility
            val humidity = thisTimeWeather.values.humidity
            val uvIndex = thisTimeWeather.values.uvIndex

            val sunrise = todayWeather.values.sunriseTime
            val sunset = todayWeather.values.sunsetTime

            val isItNight = DateTimeHelper.isItNightNow(sunrise, sunset)

            val status = WeatherCodesHelper.getStatus(context, statusCode)
            val statusIcon = WeatherCodesHelper.getIcon(context, statusCode, isItNight)

            views.setImageViewResource(R.id.widgetStatusIcon, statusIcon)
            views.setTextViewText(R.id.widgetStatus, status)
            views.setTextViewText(R.id.widgetTempUnit, unites.temperatureUnit)
            views.setTextViewText(R.id.widgetTemp, temperature)
            views.setTextViewText(R.id.widgetWindSpeedValue, windSpeed.toString() + unites.speedUnit)
            views.setTextViewText(R.id.widgetVisibilityValue, visibility.toString() + unites.distanceUnit)
            views.setTextViewText(R.id.widgetHumidityValue, humidity.toString() + "%")
            views.setTextViewText(R.id.widgetUvValue, uvIndex.toString())

            //Hourly Forecast list
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val remoteViewsCollection = RemoteViews.RemoteCollectionItems.Builder()

                for(i in 0..<hourlyForecast.size){
                    val itemView = RemoteViews(context.packageName, R.layout.weather_widget_list_card)

                    val hourIsItNight = DateTimeHelper.isItNightTime(hourlyForecast[i].time, sunrise, sunset)

                    val hourStatus = WeatherCodesHelper.getStatus(context, statusCode)
                    val hourStatusIcon = WeatherCodesHelper.getIcon(context, statusCode, hourIsItNight)

                    itemView.setImageViewResource(R.id.weatherMiniCardStatusIcon, hourStatusIcon)
                    itemView.setTextViewText(R.id.weatherMiniCardStatusValue, hourStatus)
                    itemView.setTextViewText(R.id.weatherMiniCardTempValue, Math.round(hourlyForecast[i].values.temperature).toString() + unites.temperatureUnit)

                    if(i == 0 ) itemView.setTextViewText(R.id.weatherMiniCardTimeValue, context.getString(R.string.now))
                    else itemView.setTextViewText(R.id.weatherMiniCardTimeValue, DateTimeHelper.utcToLocal(hourlyForecast[i].time, DateTimeHelper.TIME_PATTERN))

                    remoteViewsCollection.addItem(i.toLong(),itemView)
                }

                views.setRemoteAdapter(R.id.widgetHourlyForecastList, remoteViewsCollection.build())
            }

        }
    }

}

