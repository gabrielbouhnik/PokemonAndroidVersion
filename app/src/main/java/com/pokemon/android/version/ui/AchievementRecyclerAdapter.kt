package com.pokemon.android.version.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Achievement

class AchievementRecyclerAdapter(
    var activity: MainActivity,
    val data: List<Achievement>) :
    RecyclerView.Adapter<AchievementRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var achievementDescrTextView: TextView = itemView.findViewById(R.id.achievementDescrTextView)
        var claimRewardButton: Button = itemView.findViewById(R.id.claimRewardButton)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.achievement_item, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.achievementDescrTextView.text = currentItem.description
        holder.claimRewardButton.setOnClickListener{
            Toast.makeText(
                activity,
                "You cannot receive the rewards yet",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
