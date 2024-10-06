package amrk000.skyai.presentation.viewModel

import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.useCase.GetWeatherDataUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.instancio.Instancio
import org.instancio.Select.fields
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.TemporalUnit
import javax.inject.Inject
import kotlin.random.Random



@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainViewModelTest{
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var context: Application

    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var getWeatherDataUseCase: GetWeatherDataUseCase

    lateinit var viewModel: MainViewModel

    val testUtcTime = "2024-10-03T17:16:25.715358Z"
    val lat = "42.744251421489146"
    val lng = "-76.04528442565326"
    val weatherData = Instancio.of(WeatherData::class.java)
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
        hiltAndroidRule.inject()

        sharedPrefManager = mockk (relaxed = true){
            every { firstLaunch } returns true
            every { userLocation } returns UserLocation(lat, lng, "country", "city")
            every { weatherCachedData } returns CachedWeatherData(weatherData,testUtcTime)
            every { sendNotification } returns true
            every { unitsSystem } returns "metric"
        }

        getWeatherDataUseCase = mockk{
            coEvery { this@mockk.invoke(any(), any()) } returns weatherData
        }

        viewModel = MainViewModel(context, getWeatherDataUseCase, sharedPrefManager)
    }

    @Test
    fun getWeatherData() {

        runOnUiThread {
            viewModel.getWeatherDataObserver().observeForever { value ->
                Log.d(MainViewModelTest::class.simpleName, value.toString())
                assertNotNull(value)
            }

            viewModel.getWeatherData()
        }

    }

    @Test
    fun getThisWeekForecast(){
        val thisWeekForecast = viewModel.getThisWeekForecast()
        Log.d(MainViewModelTest::class.simpleName, thisWeekForecast.toString())
        assertNotNull(thisWeekForecast)
        assertFalse(thisWeekForecast!!.isEmpty())
    }

    @Test
    fun getTodayForecast(){
        val todayForecast = viewModel.getTodayForecast()
        Log.d(MainViewModelTest::class.simpleName, todayForecast.toString())
        assertNotNull(todayForecast)
    }
}