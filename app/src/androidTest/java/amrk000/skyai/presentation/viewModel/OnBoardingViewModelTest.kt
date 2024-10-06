package amrk000.skyai.presentation.viewModel

import amrk000.skyai.data.model.AiResponseData.AiResponseData
import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.useCase.AskAiUseCase
import amrk000.skyai.domain.useCase.GetUserLocationUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestScope
import org.instancio.Instancio
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OnBoardingViewModelTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var context: Application

    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var getUserLocationUseCase: GetUserLocationUseCase

    lateinit var viewModel: OnBoardingViewModel

    val testUtcTime = "2024-10-03T17:16:25.715358Z"
    val lat = "42.744251421489146"
    val lng = "-76.04528442565326"
    val weatherData = Instancio.create(WeatherData::class.java)
    val userLocationData = UserLocation(lat, lng, "country", "city")

    @Before
    fun setUp(){
        hiltAndroidRule.inject()

        sharedPrefManager = mockk{
            every { firstLaunch } returns true
            every { userLocation } returns userLocationData
            every { weatherCachedData } returns CachedWeatherData(weatherData,testUtcTime)
            every { sendNotification } returns true
            every { unitsSystem } returns "metric"
        }

        getUserLocationUseCase = mockk()

        viewModel = OnBoardingViewModel(context, sharedPrefManager, getUserLocationUseCase)
    }

    @Test
    fun getLocationTest() {
        every { getUserLocationUseCase.invoke(context) } coAnswers { flowOf(userLocationData).stateIn(TestScope(), SharingStarted.Eagerly, null) }

        runOnUiThread {
            viewModel.locationDataResultObserver().observeForever { value ->

                Log.d(OnBoardingViewModelTest::class.simpleName, value.toString())
                assertNotNull(value)

                assertTrue(viewModel.isLocationSet())
            }
        }

        viewModel.getLocation()

    }


}