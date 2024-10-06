package amrk000.skyai.domain.util

import amrk000.skyai.data.model.CachedWeatherData
import amrk000.skyai.data.model.WeatherData.WeatherData
import amrk000.skyai.domain.entity.UnitsSystem
import amrk000.skyai.domain.entity.UserLocation
import android.app.Application
import android.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SharedPrefManagerTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var context: Application

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun firstLaunchTest() {
        //default value test
        assertEquals(true, sharedPrefManager.firstLaunch)

        //changed value test
        sharedPrefManager.firstLaunch = false
        assertEquals(false, sharedPrefManager.firstLaunch)
    }

    @Test
    fun userLocation() {
        //default value test
        assertNull(sharedPrefManager.userLocation)

        //changed value test
        val userLocation = UserLocation(1.0.toString(), 1.0.toString(), "country", "city")
        sharedPrefManager.userLocation = userLocation
        assertEquals(userLocation, sharedPrefManager.userLocation)
    }

    @Test
    fun weatherCachedData() {
        //default value test
        assertNull(sharedPrefManager.weatherCachedData)

        //changed value test
        val cachedWeatherData = CachedWeatherData(WeatherData(null), Instant.now().toString())
        sharedPrefManager.weatherCachedData = cachedWeatherData
        assertEquals(cachedWeatherData, sharedPrefManager.weatherCachedData)
    }


    @Test
    fun sendNotification() {
        //default value test
        assertEquals(true, sharedPrefManager.sendNotification)

        //changed value test
        sharedPrefManager.sendNotification = false
        assertEquals(false, sharedPrefManager.sendNotification)
    }


    @Test
    fun unitsSystem() {
        //default value test
        assertEquals(UnitsSystem.SYSTEM_TYPE_METRIC, sharedPrefManager.unitsSystem)

        //changed value test
        sharedPrefManager.unitsSystem = UnitsSystem.SYSTEM_TYPE_IMPERIAL
        assertEquals(UnitsSystem.SYSTEM_TYPE_IMPERIAL, sharedPrefManager.unitsSystem)
    }

}