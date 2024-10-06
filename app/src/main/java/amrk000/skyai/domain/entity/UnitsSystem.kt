package amrk000.skyai.domain.entity

import amrk000.skyai.R
import android.content.Context

class UnitsSystem (val context: Context, val UnitsSystemType: String) {
    companion object{
        const val SYSTEM_TYPE_METRIC = "metric"
        const val SYSTEM_TYPE_IMPERIAL = "imperial"
    }

    val isImperial
        get() = UnitsSystemType == SYSTEM_TYPE_IMPERIAL

    val temperatureUnit : String
        get(){
            return if(isImperial) context.getString(R.string.F) else context.getString(R.string.C)
        }

    val distanceUnit : String
        get(){
            return if(isImperial) context.getString(R.string.mi) else context.getString(R.string.km)
        }

    val speedUnit : String
        get(){
            return if(isImperial) context.getString(R.string.mph) else context.getString(R.string.km_h)
        }

}