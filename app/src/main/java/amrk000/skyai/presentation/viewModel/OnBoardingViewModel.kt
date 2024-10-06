package amrk000.skyai.presentation.viewModel

import amrk000.skyai.domain.entity.UserLocation
import amrk000.skyai.domain.useCase.GetUserLocationUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor (
    private val application: Application,
    val sharedPrefManager: SharedPrefManager,
    val getUserLocation: GetUserLocationUseCase
    ) : AndroidViewModel(application) {

    private var locationsSet  = false

    private val userLocationResult = MutableLiveData<UserLocation?>()
    private val locationIsOffException = MutableLiveData<ResolvableApiException>()

    fun isLocationSet(): Boolean {
        return  locationsSet
    }

    fun setOnBoardingCompleted(){
        sharedPrefManager.firstLaunch = false
    }

    fun locationDataResultObserver(): LiveData<UserLocation?> {
        return userLocationResult
    }

    fun locationIsOffExceptionObserver(): LiveData<ResolvableApiException> {
        return locationIsOffException
    }

    fun setLocationData(location: UserLocation) {
        sharedPrefManager.userLocation = location
    }

    fun getLocation(){
        viewModelScope.launch {
            //Flow emits the initial value so drop ignores it
            getUserLocation.invoke(application).drop(1).collect { userLocation ->

                if(userLocation!=null) {
                    if(userLocation.locationException != null) locationIsOffException.value = userLocation.locationException
                    else {
                        userLocationResult.value = userLocation
                        locationsSet = true
                        sharedPrefManager.userLocation = userLocation
                    }
                } else userLocationResult.value = userLocation
            }

        }
    }
}