package com.pokemon.android.version

import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.pokemon.android.version.model.Character
import com.pokemon.android.version.model.Gender

class StarterSelection {

    fun startNewGame(activity : MainActivity){
        activity.setContentView(R.layout.starter_selection)
        val nextButton : Button = activity.findViewById(R.id.nextButton)
        val submitButton : Button = activity.findViewById(R.id.submitButton)
        val genderSwitch : Switch = activity.findViewById(R.id.genderSwitch)
        val characterName : EditText = activity.findViewById(R.id.characterName)
        val oakTextView : TextView = activity.findViewById(R.id.oakTextView)
        nextButton.setOnClickListener{
            oakTextView.text = "Now you can introduce yourself to me"
            nextButton.visibility = GONE ;
            submitButton.visibility = VISIBLE
            genderSwitch.visibility = VISIBLE
            characterName.visibility = VISIBLE
        }
        submitButton.setOnClickListener{
            val gender : Gender =  if (genderSwitch.isChecked()) Gender.FEMALE else Gender.MALE
            activity.character = Character(characterName.text.toString(), gender)
            activity.character!!.save()
            submitButton.visibility = GONE
            genderSwitch.visibility = GONE
            characterName.visibility = GONE
            activity.displayStarters()
            oakTextView.text = "Before starting your journey, choose one of these 3 pokemon"
        }
    }
}