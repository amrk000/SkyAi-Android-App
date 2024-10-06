package amrk000.skyai.domain.util

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
class WeatherCodesHelperTest{
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)
    @Inject
    lateinit var context: Application

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun context(){
        assertNotNull(context)
    }

    @Test
    fun getStatus(){
        //test 1
        assertEquals(context.getString(R.string.clear), WeatherCodesHelper.getStatus(context, 1000))

        //test 2
        assertEquals(context.getString(R.string.partly_cloudy),
            WeatherCodesHelper.getStatus(context, 1100)
        )

        //test 3
        assertEquals(context.getString(R.string.clear),
            WeatherCodesHelper.getStatus(context, 99999)
        )

    }

    @Test fun getIcon(){
        //test 1
        assertEquals(R.drawable.status_clear_morning_icon,
            WeatherCodesHelper.getIcon(context, 1000, false)
        )

        //test 1 : night
        assertEquals(R.drawable.status_clear_night_icon,
            WeatherCodesHelper.getIcon(context, 1000, true)
        )

        //test 3
        assertEquals(R.drawable.status_cloudy_morning_icon,
            WeatherCodesHelper.getIcon(context, 1100, false)
        )

        //test 4
        assertEquals(R.drawable.status_clear_morning_icon,
            WeatherCodesHelper.getIcon(context, 99999, false)
        )
    }
}