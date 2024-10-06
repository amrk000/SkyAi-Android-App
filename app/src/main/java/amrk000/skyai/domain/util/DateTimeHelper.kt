package amrk000.skyai.domain.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object DateTimeHelper {
    const val TIME_PATTERN = "hh:mm a"
    const val DATE_PATTERN = "dd/MM/yyyy"
    const val DAY_PATTERN = "EEEE d/M"

    fun utcToLocal(dateTimeUTC: String): ZonedDateTime {
        return Instant.parse(dateTimeUTC)
            .atZone(TimeZone.getDefault().toZoneId())
    }

    fun utcToLocal(dateTimeUTC: String, outputPattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(outputPattern)

        return utcToLocal(dateTimeUTC).format(formatter)
    }

    fun getLocalTimeOffset(): Int{
        val zoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now())
        return zoneOffset.totalSeconds / 3600
    }

    fun isItNightNow(sunriseTimeUTC: String, sunsetTimeUTC: String): Boolean{
        val currentUTC = Instant.now().toString()
        return isItNightTime(currentUTC, sunriseTimeUTC, sunsetTimeUTC)
    }

    fun isItNightTime(timeUTC: String, sunriseTimeUTC: String, sunsetTimeUTC: String): Boolean{
        val localTime = Instant.parse(timeUTC).atZone(TimeZone.getDefault().toZoneId())
        val localSunriseTime = Instant.parse(sunriseTimeUTC).atZone(TimeZone.getDefault().toZoneId())
        val localSunsetTime = Instant.parse(sunsetTimeUTC).atZone(TimeZone.getDefault().toZoneId())

        val isCurrentAfterSunrise = localTime.toLocalTime().isAfter(localSunriseTime.toLocalTime())
        val isCurrentBeforeSunset = localTime.toLocalTime().isBefore(localSunsetTime.toLocalTime())

        return !(isCurrentAfterSunrise && isCurrentBeforeSunset)
    }
}