package amrk000.skyai.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import amrk000.skyai.databinding.FragmentLoadingDialogBinding
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingDialogFragment(private val dialogMessage: String) : DialogFragment() {
    private lateinit var binding: FragmentLoadingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoadingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setDimAmount(0.7f)

        setCancelable(false)

        binding.loadingDialogMessage.text = dialogMessage

        ObjectAnimator.ofFloat(binding.loadingDialogCurve1, "rotation", 0f, -360f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        ObjectAnimator.ofFloat(binding.loadingDialogCurve2, "rotation", 0f, 360f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

    }

    override fun dismiss() {
        Handler(Looper.getMainLooper()).postDelayed({
            super.dismiss()
        },1500)
    }

}