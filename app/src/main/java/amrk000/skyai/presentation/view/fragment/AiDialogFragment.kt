package amrk000.skyai.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import amrk000.skyai.R
import amrk000.skyai.databinding.FragmentAiDialogBinding
import amrk000.skyai.data.model.ResponseData
import amrk000.skyai.presentation.viewModel.AiDialogViewModel
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AiDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAiDialogBinding
    private lateinit var viewModel: AiDialogViewModel

    val AI_QUESTION_KEY = "aiQuestion"
    val AI_GIVEN_DATA_KEY = "aiGivenData"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAiDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(AiDialogViewModel::class.java)

        ObjectAnimator.ofFloat(binding.aiDialogLoadingCurve1, "rotation", 0f, -360f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        ObjectAnimator.ofFloat(binding.aiDialogLoadingCurve2, "rotation", 0f, 360f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        binding.aiDialogLoading.visibility = View.VISIBLE
        binding.aiDialogData.visibility = View.GONE

        val aiQuestion = arguments?.getString(AI_QUESTION_KEY).toString()
        val givenData = arguments?.getString(AI_GIVEN_DATA_KEY).toString()

        binding.aiDialogQuestion.text = aiQuestion
        binding.aiDialogQuestion.isSelected = true

        viewModel.askAi(aiQuestion, givenData)

        viewModel.getAiAnswerObserver().observe(viewLifecycleOwner) { response ->
            when (response.code) {
                ResponseData.SUCCESSFUL_OPERATION -> {
                    val answer = response.candidates?.first()?.content?.parts?.first()?.text?: getString(
                        R.string.sorry_i_can_t_help_you_right_now
                    )
                    renderData(answer.replace("*",""))
                }
                ResponseData.FAILED_AUTH -> {
                    Toast.makeText(context, getString(R.string.invalid_api_key), Toast.LENGTH_LONG).show()
                }
                ResponseData.FAILED_RATE_LIMIT -> {
                    Toast.makeText(context, getString(R.string.request_limit_reached), Toast.LENGTH_LONG).show()
                }
                ResponseData.FAILED_REQUEST_FAILURE -> {
                    Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
                    Handler().postDelayed({ dismiss() },1000)
                }
                else -> {
                    Toast.makeText(context, getString(R.string.error_getting_data_form_server_code, response.code.toString()), Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun dismiss() {
        Handler(Looper.getMainLooper()).postDelayed({
            super.dismiss()
        },1500)
    }

    fun renderData(aiAnswer: String){
        binding.aiDialogLoading.animate()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(250)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                binding.aiDialogLoading.visibility = View.GONE
                binding.aiDialogData.visibility = View.VISIBLE

                binding.aiDialogData.scaleX = 0f
                binding.aiDialogData.scaleY = 0f
                binding.aiDialogData.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(250)
                    .setInterpolator(DecelerateInterpolator())

                typeWriteEffect(aiAnswer, binding.aiDialogAnswer, 30)
            }.start()
    }

    fun typeWriteEffect(text: String, textView: TextView, speedMs: Long){
        GlobalScope.launch {

        }

        lifecycleScope.launch {
            for (c in text) {
                textView.append(c.toString())
                binding.aiDialogAnswerScroll.scrollTo(0,textView.bottom)
                delay(speedMs)
            }
        }
    }

}