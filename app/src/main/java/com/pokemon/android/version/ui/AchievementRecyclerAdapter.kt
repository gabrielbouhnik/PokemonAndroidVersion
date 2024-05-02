package com.pokemon.android.version.ui

import android.graphics.Color
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
    val data: List<Achievement>
) :
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
        when {
            Achievement.isClaimable(activity, currentItem.id) -> holder.achievementDescrTextView.setTextColor(Color.RED)
            activity.trainer!!.successfulAchievements.contains(currentItem.id) -> {
                holder.achievementDescrTextView.setTextColor(Color.GRAY)
            }
            else -> {
                holder.achievementDescrTextView.setTextColor(Color.WHITE)
            }
        }
        holder.claimRewardButton.setOnClickListener {
            when {
                Achievement.isClaimable(activity, currentItem.id) -> {
                    holder.achievementDescrTextView.setTextColor(Color.GRAY)
                    holder.claimRewardButton.visibility = View.GONE
                    activity.playSoundEffect(R.raw.item_sound_effect)
                    activity.trainer!!.successfulAchievements.add(currentItem.id)
                    when {
                        currentItem.pokemonRewards.isNotEmpty() -> {
                            activity.trainer!!.receivePokemon(currentItem.pokemonRewards[0])
                            Toast.makeText(
                                activity,
                                "You got a " + currentItem.pokemonRewards[0].data.name + "!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        currentItem.itemRewards.isNotEmpty() -> {
                            Toast.makeText(
                                activity,
                                "You earn a " + activity.gameDataService.items.first{it.id == currentItem.itemRewards[0]}.name + "!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        else -> {
                            activity.trainer!!.coins += 500
                            Toast.makeText(
                                activity,
                                "You earn 500 AndroCoins!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                activity.trainer!!.successfulAchievements.contains(currentItem.id) -> {
                    Toast.makeText(
                        activity,
                        "You have already received this reward!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        activity,
                        "You can't receive this reward!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
