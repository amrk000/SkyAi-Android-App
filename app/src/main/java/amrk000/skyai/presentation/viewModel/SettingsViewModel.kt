package amrk000.skyai.presentation.viewModel

import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.useCase.GetUserLocationUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val application: Application,
    val SharedPrefManager: SharedPrefManager,
    val getUserLocation: GetUserLocationUseCase
    ) : AndroidViewModel(application) {

    private var notification = SharedPrefManager.sendNotification
    private var location = SharedPrefManager.userLocation
    private var unitsSystem = SharedPrefManager.unitsSystem

    private val userLocationResult = MutableLiveData<UserLocation?>()
    private val locationIsOffException = MutableLiveData<ResolvableApiException>()

    fun isNotificationEnabled(): Boolean {
        return notification
    }

    fun setNotificationEnabled(notification: Boolean) {
        this.notification = notification
    }

    fun getLocationData(): UserLocation? {
        return location
    }

    fun setLocationData(location: UserLocation) {
        this.location = location
    }

    fun getUnitsSystem(): String {
        return unitsSystem!!
    }

    fun setUnitsSystem(unitsSystem: String) {
        this.unitsSystem = unitsSystem
    }

    fun locationDataResultObserver(): LiveData<UserLocation?> {
        return userLocationResult
    }

    fun locationIsOffExceptionObserver(): LiveData<ResolvableApiException> {
        return locationIsOffException
    }

    fun getLocation(){
        viewModelScope.launch {
            //Flow emits the initial value so drop ignores it
            getUserLocation.invoke(application).drop(1).collect {
                userLocation ->
                if(userLocation!=null && userLocation.locationException != null) locationIsOffException.value = userLocation.locationException
                else userLocationResult.value = userLocation
            }

        }
    }
}