package com.pokemon.android.version.ui

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.utils.ItemUtils
import com.pokemon.android.version.utils.ItemUtils.Companion.POKEBALL_ID

class StarterSelection {

    fun startNewGame(activity: MainActivity) {
        activity.setContentView(R.layout.starter_selection)
        val nextButton: Button = activity.findViewById(R.id.nextButton)
        val submitButton: Button = activity.findViewById(R.id.submitButton)
        val genderSwitch: Switch = activity.findViewById(R.id.genderSwitch)
        val characterName: EditText = activity.findViewById(R.id.characterName)
        val oakTextView: TextView = activity.findViewById(R.id.oakTextView)
        nextButton.setOnClickListener {
            oakTextView.text = "Now you can introduce yourself to me"
            nextButton.visibility = GONE
            submitButton.visibility = VISIBLE
            genderSwitch.visibility = VISIBLE
            characterName.visibility = VISIBLE
        }
        submitButton.setOnClickListener {
            val gender: Gender = if (genderSwitch.isChecked) Gender.FEMALE else Gender.MALE
            activity.trainer = Trainer(characterName.text.toString(), gender)
            activity.trainer!!.addItem(POKEBALL_ID,3)
            SaveManager.save(activity)
            submitButton.visibility = GONE
            genderSwitch.visibility = GONE
            characterName.visibility = GONE
            activity.displayStarters()
            oakTextView.text = "Before starting your journey, choose one of these 3 pokemon"
        }
    }

    fun displayOakResponse(activity: MainActivity, starterName: String) {
        SaveManager.save(activity)
        val nextButton: Button = activity.findViewById(R.id.nextButton)
        val oakTextView: TextView = activity.findViewById(R.id.oakTextView)
        oakTextView.text =
            "So you chose $starterName.\nYou can now go on an adventure and become a great pokemon trainer."
        nextButton.visibility = VISIBLE
        nextButton.setOnClickListener {
            activity.updateMusic(R.raw.main_menu)
            activity.mainMenu.loadGameMenu(activity)
        }
    }
}