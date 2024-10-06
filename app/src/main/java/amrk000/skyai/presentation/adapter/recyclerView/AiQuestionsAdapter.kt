package amrk000.skyai.presentation.adapter.recyclerView

import amrk000.skyai.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AiQuestionsAdapter @Inject constructor(@ApplicationContext val context: Context) : RecyclerView.Adapter<AiQuestionsAdapter.ViewHolder>() {

    private val data = ArrayList<String>()
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val question = view.findViewById<TextView>(R.id.weatherAiRecyclerviewQuestion)
        val time = view.findViewById<TextView>(R.id.weatherAiRecyclerviewTime)
        val emoji = view.findViewById<TextView>(R.id.weatherAiRecyclerviewEmoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ai_recyclerview_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = data[position]
        holder.question.text = question.split("|")[0]
        holder.time.text = question.split("|")[1]
        holder.emoji.text = question.split("|")[2]

        holder.question.isSelected = true //activate text marquee

        holder.itemView.setOnClickListener {
            if(::onItemClickListener.isInitialized)
                onItemClickListener.onItemClick(position, data[position])
        }

    }

    fun setData(data : ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

}