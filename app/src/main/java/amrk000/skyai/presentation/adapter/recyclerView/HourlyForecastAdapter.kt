package amrk000.skyai.presentation.adapter.recyclerView

import amrk000.skyai.R
import amrk000.skyai.domain.util.DateTimeHelper
import amrk000.skyai.domain.util.WeatherCodesHelper
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class HourlyForecastAdapter @Inject constructor(@ApplicationContext val context: Context) : RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    private val data = ArrayList<amrk000.skyai.data.model.WeatherData.Hourly>()
    private lateinit var unites: amrk000.skyai.domain.entity.UnitsSystem
    private lateinit var sunRiseTime: String
    private lateinit var sunSetTime: String

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.weatherRecyclerviewStatusIcon)
        val temp = view.findViewById<TextView>(R.id.weatherRecyclerviewTempValue)
        val status = view.findViewById<TextView>(R.id.weatherRecyclerviewStatusValue)
        val time = view.findViewById<TextView>(R.id.weatherRecyclerviewTimeValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_recyclerview_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.temp.text = Math.round(data[position].values.temperature).toString() + unites.temperatureUnit
        if(position == 0 ) holder.time.text = context.getString(R.string.now)
        else holder.time.text = DateTimeHelper.utcToLocal(data[position].time, DateTimeHelper.TIME_PATTERN)

        val isItNight = DateTimeHelper.isItNightTime(data[position].time, sunRiseTime, sunSetTime)

        val weatherCode = data[position].values.weatherCode
        holder.status.text = WeatherCodesHelper.getStatus(context, weatherCode)
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, WeatherCodesHelper.getIcon(context, weatherCode, isItNight)))
    }

    fun setData(data : ArrayList<amrk000.skyai.data.model.WeatherData.Hourly>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setUnits(unites: amrk000.skyai.domain.entity.UnitsSystem) {
        this.unites = unites
    }

    fun setSunRiseTime(time: String){
        this.sunRiseTime = time
    }

    fun setSunSetTime(time: String){
        this.sunSetTime = time
    }

}