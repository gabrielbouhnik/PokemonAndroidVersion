package com.pokemon.android.version.ui

import android.text.method.ScrollingMovementMethod
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
        oakTextView.movementMethod = ScrollingMovementMethod()
        nextButton.setOnClickListener {
            oakTextView.text = "What's your name?"
            nextButton.visibility = GONE
            submitButton.visibility = VISIBLE
            genderSwitch.visibility = VISIBLE
            characterName.visibility = VISIBLE
        }
        submitButton.setOnClickListener {
            val gender: Gender = if (genderSwitch.isChecked) Gender.FEMALE else Gender.MALE
            activity.trainer = Trainer(characterName.text.toString(), gender)
            if (characterName.text.toString() == PokedexMenu.ADMIN) {
                for (pokemon in activity.gameDataService.pokemons) {
                    if (pokemon.movesByLevel.isNotEmpty())
                        activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemon(pokemon.id, 80))
                }
                activity.trainer!!.progression = 83
                activity.trainer!!.coins = 25000
                for (i in 1..100) {
                    val quantity = if (!ItemUtils.isBadge(i) && i != 30) 50 else 1
                    activity.trainer!!.addItem(i, quantity)
                }
                activity.updateMusic(R.raw.main_menu)
                activity.mainMenu.loadGameMenu(activity)
            } else {
                activity.trainer!!.addItem(POKEBALL_ID, 3)
                submitButton.visibility = GONE
                genderSwitch.visibility = GONE
                characterName.visibility = GONE
                activity.displayStarters()
                oakTextView.text = "Now, which Pokémon do you want?"
            }
        }
    }

    fun displayOakResponse(activity: MainActivity, starterName: String) {
        val nextButton: Button = activity.findViewById(R.id.nextButton)
        val oakTextView: TextView = activity.findViewById(R.id.oakTextView)
        oakTextView.movementMethod = ScrollingMovementMethod()
        oakTextView.text =
            "So $starterName is your choice.\n Your own Pokémon legend is about to unfold!\n A world of dreams and adventures awaits!"
        nextButton.visibility = VISIBLE
        nextButton.setOnClickListener {
            SaveManager.save(activity)
            activity.updateMusic(R.raw.main_menu)
            activity.mainMenu.loadGameMenu(activity)
        }
    }
}