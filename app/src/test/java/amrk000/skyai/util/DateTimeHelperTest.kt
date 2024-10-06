package amrk000.skyai.util

import amrk000.skyai.domain.util.DateTimeHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.Instant
import java.time.ZoneId
import java.util.TimeZone


@RunWith(JUnit4::class)
class DateTimeHelperTest {

    val testUtcTime = "2024-10-03T17:16:25.715358Z"
    val testSunriseUtc = "2024-10-03T07:00:00Z"
    val testSunsetUtc = "2024-10-03T19:00:00Z"

    @Test
    fun utcToLocal() {
        val expected = Instant.parse(testUtcTime).atZone(TimeZone.getDefault().toZoneId())
        assertEquals(expected, DateTimeHelper.utcToLocal(testUtcTime))
    }

    @Test
    fun utcToLocalFormatted() {
        //Test 1
        val expectedTime = "08:16 PM"
        assertEquals(expectedTime,
            DateTimeHelper.utcToLocal(testUtcTime, DateTimeHelper.TIME_PATTERN)
        )

        //Test 2
        val expectedDay = "Thursday 3/10"
        assertEquals(expectedDay,
            DateTimeHelper.utcToLocal(testUtcTime, DateTimeHelper.DAY_PATTERN)
        )
    }

    @Test
    fun getLocalTimeOffset() {
        val zoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now())
        val expected = zoneOffset.totalSeconds / 3600

        assertEquals(expected, DateTimeHelper.getLocalTimeOffset())
    }

    @Test
    fun isItNightTime() {
        var currentTimeUTC = Instant.parse(testUtcTime)

        //Test 1
        assertEquals(false,
            DateTimeHelper.isItNightTime(currentTimeUTC.toString(), testSunriseUtc, testSunsetUtc)
        )

        //Test 2
        currentTimeUTC = currentTimeUTC.plus(3, java.time.temporal.ChronoUnit.HOURS)
        assertEquals(true,
            DateTimeHelper.isItNightTime(currentTimeUTC.toString(), testSunriseUtc, testSunsetUtc)
        )
    }
}