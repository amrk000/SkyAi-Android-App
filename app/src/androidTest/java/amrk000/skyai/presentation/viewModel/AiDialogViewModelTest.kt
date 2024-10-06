package amrk000.skyai.presentation.viewModel

import amrk000.skyai.data.model.AiResponseData.AiResponseData
import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.useCase.AskAiUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import android.app.Application
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.instancio.Instancio
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AiDialogViewModelTest{
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var context: Application

    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var askAiUseCase: AskAiUseCase

    lateinit var viewModel: AiDialogViewModel

    val testUtcTime = "2024-10-03T17:16:25.715358Z"
    val lat = "42.744251421489146"
    val lng = "-76.04528442565326"
    val weatherData = Instancio.create(WeatherData::class.java)
    val aiResponseData = Instancio.create(AiResponseData::class.java)

    @Before
    fun setUp(){
        hiltAndroidRule.inject()

        sharedPrefManager = mockk{
            every { firstLaunch } returns true
            every { userLocation } returns UserLocation(lat, lng, "country", "city")
            every { weatherCachedData } returns CachedWeatherData(weatherData,testUtcTime)
            every { sendNotification } returns true
            every { unitsSystem } returns "metric"
        }

        askAiUseCase = mockk{
            coEvery { this@mockk.invoke(any()) } returns aiResponseData
        }

        viewModel = AiDialogViewModel(context, askAiUseCase, sharedPrefManager)
    }


    @Test
    fun askAi() {

        runOnUiThread {
            viewModel.getAiAnswerObserver().observeForever { value ->
                Log.d(AiDialogViewModelTest::class.simpleName, value.toString())
                assertNotNull(value)
            }

            viewModel.askAi("question?", Instancio.create(WeatherData::class.java).toString())
        }

    }
}