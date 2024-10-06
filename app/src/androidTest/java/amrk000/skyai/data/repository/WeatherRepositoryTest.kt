package amrk000.skyai.data.repository

import amrk000.skyai.domain.entity.UnitsSystem
import amrk000.skyai.domain.useCase.GetWeatherDataUseCaseTest
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class WeatherRepositoryTest{
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var weatherRepository: WeatherRepository

    val locationTest = "42.744251421489146, -76.04528442565326"

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun getWeatherData() {
        runBlocking{
            val result = weatherRepository.getWeatherData(locationTest, UnitsSystem.SYSTEM_TYPE_METRIC)

            //check result data
            assertNotNull(result)

            Log.d(GetWeatherDataUseCaseTest::class.simpleName, result.toString())
        }
    }
}