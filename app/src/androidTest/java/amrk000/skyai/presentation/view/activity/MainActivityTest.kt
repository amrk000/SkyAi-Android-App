package amrk000.skyai.presentation.view.activity

import amrk000.skyai.R
import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.data.model.ResponseData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.useCase.GetWeatherDataUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import amrk000.skyai.domain.util.WeatherCodesHelper
import amrk000.skyai.presentation.viewModel.MainViewModel
import android.app.Application
import android.icu.text.DecimalFormat
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.instancio.Instancio
import org.instancio.Select.field
import org.instancio.Select.fields
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import kotlin.math.roundToInt

import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)

//BEFORE TEST SET testMode = true  in MainActivity class
class MainActivityTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var context: Application

    val sunriseUtcTime = Instant.now().minus(Duration.ofHours(5)).toString()
    val sunsetUtcTime = Instant.now().plus(Duration.ofHours(5)).toString()

    val weatherData = Instancio.of(WeatherData::class.java)
        .set(fields().ofType(Int::class.java),Random.nextInt(99))
        .set(fields().ofType(Double::class.java),DecimalFormat("#.##").format(Random.nextDouble(99.0)).toDouble())
        .set(fields().matching("sunriseTime"), sunriseUtcTime)
        .set(fields().matching("sunsetTime"), sunsetUtcTime)
        .generate(fields().matching("time").ofType(String::class.java)) { gen ->
            gen.oneOf(
                Instant.now()
                    .plus(
                        Duration.ofDays(Random.nextLong(5))
                    )
                    .toString()
            )
        }.create()

    @Before
    fun setUp(){
        hiltRule.inject()


    }

    //BEFORE TEST SET testMode = true  in MainActivity class

    @Test
    fun dataView(){
        activityScenarioRule.scenario.onActivity { activity ->
            activity.renderWeatherData(weatherData)
        }

        onView(withId(R.id.weatherTemp)).check(
            matches(withText(
                weatherData.timelines!!.hourly[0].values.temperature.roundToInt().toString()
            ))
        )
        onView(withId(R.id.weatherStatus)).check(
            matches(withText(
                WeatherCodesHelper.getStatus(context, weatherData.timelines!!.hourly[0].values.weatherCode)
            ))
        )

        Thread.sleep(10000)
    }

    @Test
    fun liveBackgroundMorning(){
        activityScenarioRule.scenario.onActivity { activity ->
            activity.renderBackground(false)
        }

        Thread.sleep(5000)
    }

    @Test
    fun liveBackgroundNight(){
        activityScenarioRule.scenario.onActivity { activity ->
            activity.renderBackground(true)
        }

        Thread.sleep(5000)
    }

    @Test
    fun refreshSwipe(){
        activityScenarioRule.scenario.onActivity { activity ->

            GlobalScope.launch {
                onView(withId(R.id.swipeRefresh)).perform(swipeDown())
            }

        }

        Thread.sleep(5000)
    }

    @Test
    fun aboutButton(){
        onView(withId(R.id.aboutButton)).perform(click())
        Thread.sleep(1000)
    }

    @Test
    fun settingsButton(){
        onView(withId(R.id.settingsButton)).perform(click())
        Thread.sleep(1000)
    }

    @Test
    fun settingsDismiss(){
        onView(withId(R.id.settingsButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.settingsCloseButton)).perform(click())
        Thread.sleep(1000)
    }


}