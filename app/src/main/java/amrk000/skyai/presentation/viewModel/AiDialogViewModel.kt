package amrk000.skyai.presentation.viewModel

import amrk000.skyai.data.model.AiResponseData.AiResponseData
import amrk000.skyai.domain.useCase.AskAiUseCase
import amrk000.skyai.domain.util.SharedPrefManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AiDialogViewModel @Inject constructor(
    private val application: Application,
    val askAiUseCase: AskAiUseCase,
    val sharedPrefManager: SharedPrefManager
    ) : AndroidViewModel(application) {

    private var location = sharedPrefManager.userLocation
    private var unitsSystem = sharedPrefManager.unitsSystem

    private val aiAnswer = MutableLiveData<AiResponseData>()

    fun getAiAnswerObserver(): LiveData<AiResponseData> {
        return aiAnswer
    }

    fun askAi(question: String, givenData: String) {
        val requestString = StringBuilder()
        requestString.append(question)
        requestString.append(" (give brief answer based on weather data)\n")
        requestString.append("location: " + location?.city + ", " + location?.country + "language: " + Locale.getDefault().language + "\n")
        requestString.append("weather data (${unitsSystem}): \n")
        requestString.append(givenData)

        viewModelScope.launch {
            aiAnswer.value = askAiUseCase(requestString.toString())
        }
    }
}