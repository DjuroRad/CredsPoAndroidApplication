package com.example.credspoapp.ui.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.example.credspoapp.R
import com.example.credspoapp.models.UserProgress


class ProgressAdapter(private val context: Context, private val progressList: List<UserProgress>): RecyclerView.Adapter<ProgressAdapter.ProgressItemViewHolder>() {

    private val icon_drawable = context.resources.getDrawable(R.drawable.progress_icon)
    private val blank_drawable = context.resources.getDrawable(R.drawable.circle_picture)

    inner class ProgressItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val count: TextView = itemView.findViewById(R.id.countProgressTextView)
        val name: TextView = itemView.findViewById(R.id.nameProgressTextView)
        val icon: ImageView = itemView.findViewById(R.id.progressIconImageVIew)
        fun bind(activityItem: UserProgress, position: Int){
            if( activityItem.count > 0 ) {
                count.text = activityItem.count.toString()
                icon.background = icon_drawable
                count.visibility = VISIBLE
            }
            else {
                count.visibility = GONE
                icon.background = blank_drawable
            }
//            if( (position+1)%3 == 0 ) {
//                val margins:ViewGroup.MarginLayoutParams = itemView.getLayoutParams() as (ViewGroup.MarginLayoutParams) ;
//                margins.setMargins(0)
//                itemView.layoutParams = margins
//                Log.e("HERE", "$itemView")
//            }
            name.text = activityItem.name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.progress_item, parent, false)
        return ProgressItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressItemViewHolder, position: Int) {
        holder.bind(progressList[position], position)
    }

    override fun getItemCount(): Int {
        return progressList.size
    }
}