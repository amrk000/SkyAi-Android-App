package amrk000.skyai.presentation.view.activity

import amrk000.skyai.R
import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.data.model.ResponseData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.presentation.adapter.recyclerView.AiQuestionsAdapter
import amrk000.skyai.presentation.adapter.recyclerView.DailyForecastAdapter
import amrk000.skyai.presentation.adapter.recyclerView.HourlyForecastAdapter
import amrk000.skyai.databinding.ActivityMainBinding
import amrk000.skyai.domain.util.DateTimeHelper
import amrk000.skyai.domain.util.WeatherCodesHelper
import amrk000.skyai.presentation.view.fragment.AiDialogFragment
import amrk000.skyai.presentation.view.fragment.LoadingDialogFragment
import amrk000.skyai.presentation.view.fragment.SettingsDialogFragment
import amrk000.skyai.presentation.viewModel.MainViewModel
import amrk000.skyai.presentation.widget.WeatherWidget
import amrk000.skyai.presentation.widget.WeatherWidget2
import amrk000.skyai.presentation.widget.WeatherWidget3
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.Instant
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    private val AUTO_REFRESH_INTERVAL_MINS = 60
    private val WIDGETS_REFRESH_REQUEST_CODE = 0

    @Inject lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    @Inject lateinit var dailyForecastAdapter: DailyForecastAdapter
    @Inject lateinit var aiQuestionsAdapter: AiQuestionsAdapter

    lateinit var loadingWeatherData: LoadingDialogFragment

    var testMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view init
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 1, systemBars.right, 1)
            insets
        }

        //Splash Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val content = findViewById<View>(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        return false //return true to remove splash quickly
                    }
                })

            splashScreen.setOnExitAnimationListener {
                splashScreenView ->
                val exitAnimation = ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f)
                exitAnimation.interpolator = DecelerateInterpolator()
                exitAnimation.setDuration(500)

                exitAnimation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        splashScreenView.remove()
                    }
                })
                exitAnimation.start()
            }
        }

        //view model init
       viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //testing
        if(testMode){
            viewModel.SharedPrefManager.firstLaunch = false
            viewModel.setCachedWeatherData(CachedWeatherData(WeatherData(null), Instant.now().toString()))
        }

        //init background service
        if(!testMode) viewModel.initBackgroundService()

        //if first launch start onBoarding
        if(viewModel.isFirstLaunch()){
            finish()
            startActivity(Intent(this, OnBoardingActivity::class.java))
        }

        //Render Activity UI

        //Weather Data
        binding.weatherHourlyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.weatherHourlyRecyclerView.adapter = hourlyForecastAdapter

        binding.weatherDailyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.weatherDailyRecyclerView.adapter = dailyForecastAdapter

        val cachedWeatherData = viewModel.getCachedWeatherData()

        loadingWeatherData = LoadingDialogFragment(getString(R.string.looking_at_sky))

        if(cachedWeatherData == null){
            binding.mainUI.visibility = View.INVISIBLE
            loadingWeatherData.show(supportFragmentManager, getString(R.string.loading))
            viewModel.getWeatherData()
        }
        else{
            renderWeatherData(cachedWeatherData.weatherData)
            val lastUpdateTime = Instant.parse(cachedWeatherData.time)
            val currentTime = Instant.now()
            val duration = Duration.between(lastUpdateTime, currentTime)

            if(duration.toMinutes() >= AUTO_REFRESH_INTERVAL_MINS){
                binding.mainLoadingDataPorgress.visibility = View.VISIBLE
                viewModel.getWeatherData()
            }
        }

        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this,R.color.black))
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.mainColor))

        binding.swipeRefresh.setOnRefreshListener {
            binding.noInternetNotif.visibility = View.INVISIBLE
            viewModel.getWeatherData()
        }

        viewModel.getWeatherDataObserver().observe(this) { response ->
            if(loadingWeatherData.isVisible) loadingWeatherData.dismiss()
            binding.mainUI.visibility = View.VISIBLE
            binding.swipeRefresh.isRefreshing = false

            binding.mainLoadingDataPorgress.visibility = View.INVISIBLE

            when (response.code) {
                ResponseData.SUCCESSFUL_OPERATION -> {
                    renderWeatherData(response)
                    refreshWidgets()
                }
                ResponseData.FAILED_AUTH -> {
                    Toast.makeText(applicationContext, getString(R.string.invalid_api_key), Toast.LENGTH_LONG).show()
                }
                ResponseData.FAILED_RATE_LIMIT -> {
                    Toast.makeText(applicationContext, getString(R.string.request_limit_reached), Toast.LENGTH_LONG).show()
                }
                ResponseData.FAILED_REQUEST_FAILURE -> {
                    binding.noInternetNotif.visibility = View.VISIBLE
                }
                else -> {
                    Toast.makeText(applicationContext, getString(R.string.error_getting_data_form_server_code, response.code.toString()), Toast.LENGTH_LONG).show()
                }
            }
        }

        //Ai Section
        ObjectAnimator.ofFloat(binding.weatherAiCurve1, "rotation", 0f, -360f).apply {
            duration = 15000
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        ObjectAnimator.ofFloat(binding.weatherAiCurve2, "rotation", 0f, 360f).apply {
            duration = 15000
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        binding.weatherAiRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.weatherAiRecyclerView.adapter = aiQuestionsAdapter

        val questions = ArrayList<String>(resources.getStringArray(R.array.aiQuestions).toList())
        aiQuestionsAdapter.setData(questions)

        aiQuestionsAdapter.setOnItemClickListener(object: AiQuestionsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: String) {
                val question = data.split("|")[0]
                val time = data.split("|")[1]

                val fullQuestion = question.replace("?", " $time?").replace("؟", " $time؟")

                lateinit var givenData: String
                if(time == "today"){
                    val hourlyData = viewModel.getTodayForecast()?.map{ it.copy() }

                    givenData = Gson().toJson(hourlyData?.onEach {
                        data -> data.time = DateTimeHelper.utcToLocal(data.time).toString()
                    })

                }
                else givenData = Gson().toJson(viewModel.getThisWeekForecast())

                askAi(fullQuestion, givenData)
            }
        })

        binding.settingsButton.setOnClickListener {
           val x = SettingsDialogFragment()
            x.show(supportFragmentManager, "settings")

        }

        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

    }

    fun renderBackground(renderNight : Boolean){
        val uri : Uri

        if(renderNight) {
            binding.backgroundColor.setImageResource(R.drawable.nightbg)
            uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.nightvidbg)
        }
        else uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.dayvidbg)

        binding.backgroundVideo.setVideoURI(uri)
        binding.backgroundVideo.setOnPreparedListener {
            it.isLooping = true
            it.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            it.playbackParams = it.playbackParams.setSpeed(1.1f)

            binding.backgroundVideo.start()

            val objectAnimator = ObjectAnimator.ofFloat(binding.backgroundVideo, "alpha", 0f, 0.6f)
            objectAnimator.setDuration(2500)
            objectAnimator.start()
        }
    }

    fun renderWeatherData(weatherData : WeatherData){
        binding.weatherLocation.text = viewModel.getLocationData()?.city.toString() + ", " + viewModel.getLocationData()?.country.toString()

        val unites = viewModel.getUnits()

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

            val feelsLike = Math.round(todayWeather.values.temperatureApparentAvg).toString()
            val sunrise = todayWeather.values.sunriseTime
            val sunset = todayWeather.values.sunsetTime

            val isItNight = DateTimeHelper.isItNightNow(sunrise, sunset)
            renderBackground(isItNight)

            binding.weatherStatus.text = WeatherCodesHelper.getStatus(this, statusCode)
            binding.weatherStatusIcon.setImageDrawable(ContextCompat.getDrawable(this, WeatherCodesHelper.getIcon(this, statusCode, isItNight)))
            binding.weatherTempUnit.text = unites.temperatureUnit
            binding.weatherTemp.text = temperature
            binding.weatherFeelsLike.text = getString(R.string.feels_like) + feelsLike + unites.temperatureUnit
            binding.weatherWindSpeedValue.text = windSpeed.toString() + unites.speedUnit
            binding.weatherVisibilityValue.text = visibility.toString() + unites.distanceUnit
            binding.weatherHumidityValue.text = humidity.toString() + "%"
            binding.weatherUvValue.text = uvIndex.toString()
            binding.weatherSunRiseValue.text = DateTimeHelper.utcToLocal(sunrise, DateTimeHelper.TIME_PATTERN)
            binding.weatherSunSetValue.text = DateTimeHelper.utcToLocal(sunset, DateTimeHelper.TIME_PATTERN)

            ObjectAnimator.ofInt(binding.timeProgressBar,
                "progress",
                0,
                sunProgress(sunrise, sunset))
                .apply {
                    duration = 5000
                    interpolator = DecelerateInterpolator()
                }.start()

            hourlyForecastAdapter.setData(hourlyForecast)
            hourlyForecastAdapter.setUnits(unites)
            hourlyForecastAdapter.setSunRiseTime(sunrise)
            hourlyForecastAdapter.setSunSetTime(sunset)

            dailyForecastAdapter.setData(dailyForecast)
            dailyForecastAdapter.setUnits(unites)
            dailyForecastAdapter.setNight(isItNight)

        }
    }

    fun sunProgress(sunriseTimeUTC: String, sunsetTimeUTC: String): Int {
        val localSunriseTime = DateTimeHelper.utcToLocal(sunriseTimeUTC)
        val localSunsetTime = DateTimeHelper.utcToLocal(sunsetTimeUTC)

        val totalHours = Duration.between(localSunriseTime, localSunsetTime).toHours()
        val hoursPassed = Duration.between(localSunriseTime, Instant.now().atZone(TimeZone.getDefault().toZoneId())).toHours()

        return (hoursPassed*100/totalHours).toInt()
    }

    fun askAi(question: String, givenData: String){
        val aiDialog = AiDialogFragment()
        aiDialog.setStyle(R.style.BottomSheetStyle, R.style.BottomSheetStyle)

        val bundle = Bundle()
        bundle.putString(aiDialog.AI_QUESTION_KEY, question)
        bundle.putString(aiDialog.AI_GIVEN_DATA_KEY, givenData)

        aiDialog.arguments = bundle
        aiDialog.show(supportFragmentManager, aiDialog.tag)
    }

    fun showLoadingProgress(){
        binding.mainLoadingDataPorgress.visibility = View.VISIBLE
    }

    private fun refreshWidgets() {
        val widgets = arrayOf(WeatherWidget::class.java, WeatherWidget2::class.java, WeatherWidget3::class.java)

        for(widget in widgets) {
            val widgetIntent = Intent(this, widget)
            widgetIntent.setAction(WeatherWidget.ACTION_REFRESH)

            val widgetPendingIntent = PendingIntent.getBroadcast(this, WIDGETS_REFRESH_REQUEST_CODE, widgetIntent, PendingIntent.FLAG_IMMUTABLE)
            widgetPendingIntent.send()
        }
    }

}