package amrk000.skyai.presentation.adapter.viewPager

import amrk000.skyai.R

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


//multiView adapter
class OnBoardingViewPagerAdapter @Inject constructor(@ApplicationContext val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = ArrayList<amrk000.skyai.domain.entity.OnBoardingPage>()

    private val startPageData =
        amrk000.skyai.domain.entity.OnBoardingPage(0, "", "") //dummy object for start page
    private val START_HOLDER_VIEW = 0

    private val endPageData =
        amrk000.skyai.domain.entity.OnBoardingPage(0, "", "") //dummy object for end page
    private val END_HOLDER_VIEW = -1

    //Handling end page buttons clicks
    private lateinit var endPageButtonClickListener : EndPageButtonClickListener

    interface EndPageButtonClickListener {
        fun onNotificationClicked(button: MaterialButton)
        fun onLocationClicked(button: MaterialButton)
    }

    class StartHolder(view: View) : RecyclerView.ViewHolder(view)

    class EndHolder(view: View) : RecyclerView.ViewHolder(view){
        val notificationButton = view.findViewById<MaterialButton>(R.id.onBoardingAllowNotificationButton)
        val locationButton = view.findViewById<MaterialButton>(R.id.onBoardingSetLocationButton)
    }

    class PageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.onBoardingPageImgae)
        val title = view.findViewById<TextView>(R.id.onBoardingPageTitle)
        val desc = view.findViewById<TextView>(R.id.onBoardingPageDesc)
    }

    override fun getItemViewType(position: Int): Int {
        if (data.get(position) === startPageData) return START_HOLDER_VIEW
        else if (data.get(position) === endPageData) return END_HOLDER_VIEW
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            START_HOLDER_VIEW -> return StartHolder(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_start, parent, false))
            END_HOLDER_VIEW -> return EndHolder(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_end, parent, false))
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.onboarding_page, parent, false)
        return PageHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == START_HOLDER_VIEW){
            val viewHolder = (holder as StartHolder)

        }
        else if (getItemViewType(position) == END_HOLDER_VIEW){
            val viewHolder = (holder as EndHolder)

            viewHolder.notificationButton.setOnClickListener {
                if(::endPageButtonClickListener.isInitialized) endPageButtonClickListener.onNotificationClicked(viewHolder.notificationButton)
            }

            viewHolder.locationButton.setOnClickListener {
                if(::endPageButtonClickListener.isInitialized) endPageButtonClickListener.onLocationClicked(viewHolder.locationButton)
            }
        }
        else{
            val viewHolder = (holder as PageHolder)
            viewHolder.image.setImageResource(data.get(position).imageId)
            viewHolder.title.text = data.get(position).title
            viewHolder.desc.text = data.get(position).desc
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.bindingAdapterPosition

        if (getItemViewType(position) == START_HOLDER_VIEW){
            val viewHolder = (holder as StartHolder)

            viewHolder.itemView.scaleX = 0.7f
            viewHolder.itemView.scaleY = 0.7f

            viewHolder.itemView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(1500)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        else if (getItemViewType(position) == END_HOLDER_VIEW){
            val viewHolder = (holder as EndHolder)

            viewHolder.itemView.scaleX = 0.7f
            viewHolder.itemView.scaleY = 0.7f

            viewHolder.itemView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(500)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        else{

            val viewHolder = (holder as PageHolder)

            viewHolder.title.alpha = 0.0f
            viewHolder.desc.alpha = 0.0f

            viewHolder.image.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)
                .setDuration(1500)
                .setInterpolator(DecelerateInterpolator())
                .start()

            viewHolder.title.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setInterpolator(DecelerateInterpolator())
                .setStartDelay(250)
                .start()

            viewHolder.desc.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setInterpolator(DecelerateInterpolator())
                .setStartDelay(500)
                .start()

        }
    }

    fun initData(data : ArrayList<amrk000.skyai.domain.entity.OnBoardingPage>) {
        this.data.add(startPageData)
        this.data.addAll(data)
        this.data.add(endPageData)
    }

    fun setEndPageButtonClickListener(listener: EndPageButtonClickListener) {
        endPageButtonClickListener = listener
    }

}