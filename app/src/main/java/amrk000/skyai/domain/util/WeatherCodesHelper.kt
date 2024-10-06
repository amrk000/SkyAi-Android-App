package amrk000.skyai.domain.util

import amrk000.skyai.R
import android.content.Context
import androidx.core.content.ContextCompat

//Weather codes: https://docs.tomorrow.io/reference/data-layers-weather-codes
object WeatherCodesHelper {

    fun getStatus(context: Context, code: Int): String {
        return when(code){
            1000 -> context.getString(R.string.clear)
            1100, 1101, 1102 -> context.getString(R.string.partly_cloudy)
            1001 -> context.getString(R.string.cloudy)
            2000, 2100 -> context.getString(R.string.foggy)
            4000, 4200, 4001, 4201 -> context.getString(R.string.raining)
            5001, 5100, 5000, 5101 -> context.getString(R.string.snowing)
            6000, 6200, 6001, 6201 -> context.getString(R.string.freezing_drizzle)
            7102, 7000, 7101 -> context.getString(R.string.ice_pellets)
            8000 -> context.getString(R.string.thunder)
            else -> context.getString(R.string.clear)
        }
    }

    fun getIcon(context: Context, code: Int, nightIcon: Boolean): Int {
        return when(code){
            1000 -> {
                if(nightIcon) R.drawable.status_clear_night_icon
                else R.drawable.status_clear_morning_icon
            }
            1100, 1101, 1102 -> {
                if(nightIcon) R.drawable.status_cloudy_night_icon
                else R.drawable.status_cloudy_morning_icon
            }
            1001, 2000, 2100 -> R.drawable.status_full_cloud_icon
            4000, 4200, 4001, 4201 -> R.drawable.status_raining_icon
            5001, 5100, 5000, 5101, 6000, 6200, 6001, 6201, 7102, 7000, 7101 -> R.drawable.status_snow_icon
            8000 -> R.drawable.status_raining_sparks_icon
            else -> {
                if(nightIcon) R.drawable.status_clear_night_icon
                else R.drawable.status_clear_morning_icon
            }
        }
    }
}