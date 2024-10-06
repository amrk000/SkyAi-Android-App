package amrk000.skyai.presentation.view.fragment

import amrk000.skyai.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import amrk000.skyai.databinding.FragmentSettingsDialogBinding
import amrk000.skyai.domain.entity.UnitsSystem
import amrk000.skyai.presentation.view.activity.MainActivity
import amrk000.skyai.presentation.viewModel.MainViewModel
import amrk000.skyai.presentation.viewModel.SettingsViewModel
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentSettingsDialogBinding
    private lateinit var viewModel: SettingsViewModel
    private val activityViewModel: MainViewModel by activityViewModels()

    private lateinit var requestTurnOnLocation: ActivityResultLauncher<IntentSenderRequest>

    private lateinit var loadingDialogFragment: LoadingDialogFragment

    private var unitsSystemChanged = false
    private var locationChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //view model init
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        loadingDialogFragment = LoadingDialogFragment(getString(R.string.updating_location))

        //render defaults
        binding.settingsSendNotificationSwitch.isChecked = viewModel.isNotificationEnabled()

        when(viewModel.getUnitsSystem()){
            UnitsSystem.SYSTEM_TYPE_METRIC -> binding.settingsChipGroup.check(R.id.settingsMetricChip)
            UnitsSystem.SYSTEM_TYPE_IMPERIAL -> binding.settingsChipGroup.check(R.id.settingsImperialChip)
        }

        binding.settingsCurrentLocation.text = getString(R.string.current) + "${viewModel.getLocationData()?.city}, ${viewModel.getLocationData()?.country}"

        //changes listener
        binding.settingsSendNotificationSwitch.setOnCheckedChangeListener{ switch, checked ->
            viewModel.setNotificationEnabled(checked)
        }

        binding.settingsChipGroup.setOnCheckedChangeListener { _, checkedId ->
            lateinit var selectedUnitsSystem : String

            selectedUnitsSystem = when(checkedId){
                R.id.settingsMetricChip -> UnitsSystem.SYSTEM_TYPE_METRIC
                R.id.settingsImperialChip -> UnitsSystem.SYSTEM_TYPE_IMPERIAL
                else -> UnitsSystem.SYSTEM_TYPE_METRIC
            }

            unitsSystemChanged = selectedUnitsSystem != viewModel.getUnitsSystem()
            viewModel.setUnitsSystem(selectedUnitsSystem)
        }

        binding.settingsSetLocationButton.setOnClickListener {
            getLocationData()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            binding.settingsChangeLanguageButton.setOnClickListener {
                val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
                intent.data = Uri.fromParts("package", context?.packageName, null)
                startActivity(intent)
            }
        }
        else {
            binding.settingsTextView4.visibility = View.GONE
            binding.settingsChangeLanguageButton.visibility = View.GONE
        }

        binding.settingsCloseButton.setOnClickListener {
            dismiss()
        }

        requestTurnOnLocation =  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                result ->
            if (result.resultCode == RESULT_OK) {
                getLocationData()
            }
        }

        //Location
        //Location Results
        viewModel.locationDataResultObserver().observe(this) { data ->

            if(loadingDialogFragment.isVisible) loadingDialogFragment.dismiss()

            if(data == null) Toast.makeText(context, getString(R.string.error_please_turn_on_internet_gps), Toast.LENGTH_LONG).show()
            else {
                binding.settingsCurrentLocation.text = getString(R.string.current) + "${data.city}, ${data.country}"
                viewModel.setLocationData(data)
                locationChanged = true
            }
        }

        viewModel.locationIsOffExceptionObserver().observe(this) { exception ->
            if(loadingDialogFragment.isVisible) loadingDialogFragment.dismiss()

                //location is off request to turn it on
                try {
                    requestTurnOnLocation.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )

                } catch (sendEx: IntentSender.SendIntentException) {
                    //dialog not launched so open location settings page
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }

        }


    }

    fun getLocationData(){
        loadingDialogFragment.show(getParentFragmentManager(), loadingDialogFragment.tag)
        viewModel.getLocation()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        //Apply Changes
        val mainActivity = activity as MainActivity

        activityViewModel.enableNotification(viewModel.isNotificationEnabled())
        if(unitsSystemChanged) activityViewModel.setUnitsSystem(viewModel.getUnitsSystem())
        if(locationChanged) activityViewModel.setLocationData(viewModel.getLocationData()!!)
        if(unitsSystemChanged || locationChanged) {
            mainActivity.showLoadingProgress()
            activityViewModel.getWeatherData()
        }
    }
}