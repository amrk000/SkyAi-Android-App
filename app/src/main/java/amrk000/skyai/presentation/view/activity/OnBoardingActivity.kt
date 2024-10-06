package amrk000.skyai.presentation.view.activity

import amrk000.skyai.R
import amrk000.skyai.presentation.adapter.viewPager.OnBoardingViewPagerAdapter
import amrk000.skyai.databinding.ActivityOnBoardingBinding
import amrk000.skyai.presentation.view.fragment.LoadingDialogFragment
import amrk000.skyai.presentation.viewModel.OnBoardingViewModel
import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var viewModel: OnBoardingViewModel

    private lateinit var requestNotification: ActivityResultLauncher<String>
    private lateinit var requestLocation: ActivityResultLauncher<Array<String>>
    private lateinit var requestTurnOnLocation: ActivityResultLauncher<IntentSenderRequest>

    private lateinit var endPageNotifButton: MaterialButton
    private lateinit var endPageLocationButton: MaterialButton

    private lateinit var loadingDialogFragment: LoadingDialogFragment

    @Inject lateinit var adapter: OnBoardingViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view init
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 1, systemBars.right, 1)
            insets
        }

        viewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)

        //render activity
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.dayvidbg)
        binding.onBoardingBackgroundVideo.setVideoURI(uri)
        binding.onBoardingBackgroundVideo.setOnPreparedListener {
            it.isLooping = true
            it.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            it.playbackParams = it.playbackParams.setSpeed(1.7f)
            binding.onBoardingBackgroundVideo.start()

            ObjectAnimator.ofFloat(binding.onBoardingBackgroundVideo, "alpha", 0f, 0.5f)
            .setDuration(2000)
            .start()
        }

        val objectAnimator = ObjectAnimator.ofFloat(binding.onBoardingDim, "alpha", 0f, 1.0f).setDuration(2500)
        objectAnimator.startDelay = 1000
        objectAnimator.addListener( onStart = {
            binding.onBoardingDim.visibility = View.VISIBLE
        })
        objectAnimator.start()

        //intro view pager
        val data: ArrayList<amrk000.skyai.domain.entity.OnBoardingPage> = ArrayList()

        data.add(
            amrk000.skyai.domain.entity.OnBoardingPage(
                R.drawable.onboarding1,
                getString(R.string.onboarding_1_title),
                getString(R.string.onboarding_1_desc)
            )
        )

        data.add(
            amrk000.skyai.domain.entity.OnBoardingPage(
                R.drawable.onboarding2,
                getString(R.string.onboarding_2_title),
                getString(R.string.onboarding_2_desc)
            )
        )

        data.add(
            amrk000.skyai.domain.entity.OnBoardingPage(
                R.drawable.onboarding3,
                getString(R.string.onboarding_3_title),
                getString(R.string.onboarding_3_desc)
            )
        )

        adapter.initData(data)
        binding.onBoardingViewPager.adapter = adapter

        val indicator = TabLayoutMediator(binding.onBoardingIndicator, binding.onBoardingViewPager) { tab, position -> {}}
        indicator.attach()

        binding.onBoardingViewPager.registerOnPageChangeCallback(
            object: OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == adapter.itemCount - 1) binding.onBoardingButton.text = getString(R.string.start_app)
                    else binding.onBoardingButton.text = getString(R.string.next)
                }
            }
        )

        binding.onBoardingButton.setOnClickListener {
            if (binding.onBoardingViewPager.currentItem == adapter.itemCount - 1) {
                if(viewModel.isLocationSet()) {
                    viewModel.setOnBoardingCompleted()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                else Toast.makeText(applicationContext,
                    getString(R.string.please_set_location_first), Toast.LENGTH_SHORT).show()
            }
            else binding.onBoardingViewPager.currentItem += 1
        }

        //Handling onboarding end page
        requestNotification = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            granted ->
                if(granted) {
                    endPageNotifButton.apply {
                        icon = ContextCompat.getDrawable(applicationContext, R.drawable.baseline_check_circle_outline_24)
                        text = context.getString(R.string.done)
                        isEnabled = false
                        alpha = 0.7f
                    }
                }
                else Toast.makeText(applicationContext, "Notification not allowed", Toast.LENGTH_SHORT).show()
        }

        requestLocation = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
                if(permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                    endPageLocationButton.performClick() //location is allowed perform button click again to set location
                }
                else Toast.makeText(applicationContext, "Location not allowed", Toast.LENGTH_SHORT).show()
        }

        requestTurnOnLocation =  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                getLocationData()
            }
        }

        adapter.setEndPageButtonClickListener(object: OnBoardingViewPagerAdapter.EndPageButtonClickListener {
            override fun onNotificationClicked(button: MaterialButton) {
                endPageNotifButton = button

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotification.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            override fun onLocationClicked(button: MaterialButton) {
                endPageLocationButton = button

                val fineLocation = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                val coarseLocation = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                if(fineLocation && coarseLocation) getLocationData()
                else requestLocation.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))

            }
        })

        //Location Results
        viewModel.locationDataResultObserver().observe(this) {
            data ->
            if(loadingDialogFragment.isVisible) loadingDialogFragment.dismiss()

            if(data == null) Toast.makeText(applicationContext, "Error: Please turn on internet & GPS", Toast.LENGTH_LONG).show()
            else {
                endPageLocationButton.apply {
                    icon = ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.baseline_check_circle_outline_24
                    )
                    text = "${data.city}, ${data.country}"
                    isEnabled = false
                    alpha = 0.7f
                }
            }
        }

        viewModel.locationIsOffExceptionObserver().observe(this) {
            exception ->
            if(loadingDialogFragment.isVisible) loadingDialogFragment.dismiss()

            //location is off request to turn it on
            try {
                requestTurnOnLocation.launch(IntentSenderRequest.Builder(exception.resolution).build())

            } catch (sendEx: IntentSender.SendIntentException) {
                //dialog not launched so open location settings page
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        binding.onBoardingBackgroundVideo.start()

    }

    override fun onPause() {
        super.onPause()
        binding.onBoardingBackgroundVideo.pause()
    }

    fun getLocationData(){
        loadingDialogFragment = LoadingDialogFragment(getString(R.string.loading_location))
        loadingDialogFragment.show(supportFragmentManager, loadingDialogFragment.tag)

        viewModel.getLocation()
    }

}