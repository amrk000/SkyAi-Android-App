package amrk000.skyai.domain.useCase

import amrk000.skyai.domain.entity.UserLocation
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor() {

    @SuppressLint("MissingPermission")
    operator fun invoke(context: Context): StateFlow<UserLocation?> {
        val result = MutableStateFlow<UserLocation?>(UserLocation("", "", "", ""))

        val locationRequest = LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY,1000).build()

        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()

        LocationServices.getSettingsClient(context).checkLocationSettings(settingsBuilder)
            .addOnSuccessListener {
                //location is on lets get data

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

                fusedLocationClient.getCurrentLocation(locationRequest.priority, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                    override fun isCancellationRequested() = false
                })
                    .addOnSuccessListener { location ->

                        if(location != null) {

                            val geocoder = Geocoder(context, Locale.forLanguageTag(Locale.getDefault().toLanguageTag()))
                            var address: List<Address>? = null

                            try {
                                address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            }
                            catch (exception: IOException) {
                                    exception.printStackTrace()
                                    Log.d("location address exception: ", exception.message.toString())
                            }

                            if (address != null) {
                                val country: String = address[0].countryName ?: ""
                                val city: String = address[0].subAdminArea ?: address[0].adminArea ?: ""

                                result.value = UserLocation(
                                    location.latitude.toString(),
                                    location.longitude.toString(),
                                    country,
                                    city
                                )
                            }

                        } else result.value = location


                    }

            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                Log.d("location exception: ", exception.message.toString())

                if (exception is ResolvableApiException) {
                    val location = UserLocation("0", "0", "unknown", "unknown")
                    location.locationException = exception
                    result.value = location
                }

            }

        return result
    }

}