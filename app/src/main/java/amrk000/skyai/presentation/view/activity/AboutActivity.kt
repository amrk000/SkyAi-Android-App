package amrk000.skyai.presentation.view.activity

import amrk000.skyai.BuildConfig
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import amrk000.skyai.databinding.ActivityAboutBinding
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.animation.DecelerateInterpolator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding init
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot()) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        binding.aboutCreditCard.visibility = View.INVISIBLE

        binding.aboutAppVersion.text = "Version " + BuildConfig.VERSION_NAME

        binding.moreapps.setOnClickListener { v ->
            val profilelink = Intent(Intent.ACTION_VIEW)
            profilelink.setData(Uri.parse("https://play.google.com/store/apps/dev?id=5289896800613171020"))
            startActivity(profilelink)
        }

        binding.aboutAppRepoButton.setOnClickListener { v ->
            val repoLink = Intent(Intent.ACTION_VIEW)
            repoLink.setData(Uri.parse("#"))
            startActivity(repoLink)
        }

        binding.aboutCloseButton.setOnClickListener {
            onBackPressed()
        }

        binding.aboutCreditCard.visibility = View.VISIBLE
        binding.aboutCreditCard.y = binding.aboutCreditCard.y + 1000

        binding.aboutCreditCard.animate()
            .translationY(binding.aboutCreditCard.y - 1000f)
            .setInterpolator(DecelerateInterpolator())
            .setStartDelay(500)
            .setDuration(1000)
            .start()

    }
}