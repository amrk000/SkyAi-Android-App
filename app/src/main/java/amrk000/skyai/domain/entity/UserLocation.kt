package amrk000.skyai.domain.entity

import com.google.android.gms.common.api.ResolvableApiException
import com.google.gson.annotations.SerializedName

data class UserLocation (
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lng: String,
    @SerializedName("country") val country: String,
    @SerializedName("city") val city: String
){
    var locationException: ResolvableApiException? = null
}
