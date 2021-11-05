package com.example.credspoapp.ui.activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.credspoapp.R
import com.example.credspoapp.models.ActivityCredsPo
import com.example.credspoapp.ui.home.getBearerToken

class ActivityHistoryAdapter(private val context: Context, private val activitiesList: List<ActivityCredsPo>, private val onDeleteClickListener: OnDeleteActivityListener?): RecyclerView.Adapter<ActivityHistoryAdapter.ActivityItemViewHolder>() {

    private var bearerToken: String? = ""

    inner class ActivityItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dateView: TextView = itemView.findViewById(R.id.dateTextView)
        val activityNameView: TextView = itemView.findViewById(R.id.activityNameTextVIew)
        val deleteActivityButton: ImageView = itemView.findViewById(R.id.deleteActivityIcon)
        fun bind(activityItem: ActivityCredsPo){
            dateView.text = activityItem.date
            activityNameView.text = activityItem.title
            deleteActivityButton.setOnClickListener {
                //here delete the activity by calling the api!
                Log.e("BIND DEL CLICK", "${activityItem.id}")
                onDeleteClickListener?.onDeleteActivity(activityItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_history_item, parent, false)
        bearerToken = getBearerToken(context)
        return ActivityItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityItemViewHolder, position: Int) {
        holder.bind(activitiesList[position])
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    interface OnDeleteActivityListener{
        fun onDeleteActivity(activityItem: ActivityCredsPo)
    }
}