package amrk000.skyai.domain.entity

import amrk000.skyai.R
import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UnitsSystemTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)
    @Inject
    lateinit var context: Application

    lateinit var unitsSystem: UnitsSystem

    val system = UnitsSystem.SYSTEM_TYPE_METRIC

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        unitsSystem = UnitsSystem(context, system)
    }

    @Test
    fun context(){
        assertNotNull(context)
    }

    @Test
    fun isImperial() {
        when(system){
            UnitsSystem.SYSTEM_TYPE_IMPERIAL -> assertEquals(true, unitsSystem.isImperial)
            UnitsSystem.SYSTEM_TYPE_METRIC -> assertEquals(false, unitsSystem.isImperial)
        }
    }

    @Test
    fun getTemperatureUnit() {
        when(system){
            UnitsSystem.SYSTEM_TYPE_IMPERIAL -> assertEquals(context.getString(R.string.F), unitsSystem.temperatureUnit)
            UnitsSystem.SYSTEM_TYPE_METRIC -> assertEquals(context.getString(R.string.C), unitsSystem.temperatureUnit)
        }
    }

    @Test
    fun getDistanceUnit() {
        when(system){
            UnitsSystem.SYSTEM_TYPE_IMPERIAL -> assertEquals(context.getString(R.string.mi), unitsSystem.distanceUnit)
            UnitsSystem.SYSTEM_TYPE_METRIC -> assertEquals(context.getString(R.string.km), unitsSystem.distanceUnit)
        }
    }

    @Test
    fun getSpeedUnit() {
        when(system){
            UnitsSystem.SYSTEM_TYPE_IMPERIAL -> assertEquals(context.getString(R.string.mph), unitsSystem.speedUnit)
            UnitsSystem.SYSTEM_TYPE_METRIC -> assertEquals(context.getString(R.string.km_h), unitsSystem.speedUnit)
        }
    }

}