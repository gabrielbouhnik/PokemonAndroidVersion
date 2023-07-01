package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Pokemon

class PokemonMenu {
    var pokemonInfoMenu = PokemonInfoMenu(R.layout.pokemons_menu)

    fun loadPokemonMenuElite(activity: MainActivity) {
        activity.setContentView(R.layout.pokemons_menu)
        val backButton: Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            pokemonInfoMenu.loadPokemonInfoLayout(activity, activity.trainer!!.pokemons[position])
        }
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team).toMutableList()
        val adapter = PokemonRecyclerAdapter(activity, pokemons, myItemClickListener, true)
        recyclerView.adapter = adapter
        val teamButton: Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.setOnClickListener {
            loadTeamMenuElite(activity)
        }
    }

    private fun loadTeamMenuElite(activity: MainActivity) {
        activity.setContentView(R.layout.team_layout)
        val teamSize = activity.trainer!!.team.size
        val backButton: Button = activity.findViewById(R.id.teamMenuBackButton)
        backButton.setOnClickListener {
            if (activity.trainer!!.team.size == teamSize) {
                loadPokemonMenuElite(activity)
            } else
                Toast.makeText(activity, "You need to select all your Pokemon.", Toast.LENGTH_SHORT).show()
        }
        val teamRecyclerView = activity.findViewById<RecyclerView>(R.id.teamRecyclerView)
        teamRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemonsRecyclerView = activity.findViewById<RecyclerView>(R.id.pokemonsRecyclerView)
        pokemonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team).toMutableList()
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (activity.trainer!!.team.size < teamSize && !activity.trainer!!.team.contains(pokemons[position])) {
                activity.trainer!!.team.add(pokemons[position])
                teamRecyclerView.adapter =
                    TeamMemberRecyclerAdapter(activity, activity.trainer!!.team) {}
            }
        }
        pokemonsRecyclerView.adapter = TeamMemberRecyclerAdapter(activity, pokemons, myItemClickListener)
        activity.trainer!!.team.clear()
    }


    fun loadPokemonMenu(activity: MainActivity) {
        activity.setContentView(R.layout.pokemons_menu)
        val backButton: Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            pokemonInfoMenu.loadPokemonInfoLayout(activity, activity.trainer!!.pokemons[position])
        }
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team + activity.trainer!!.pokemons.filter {
            !activity.trainer!!.team.contains(it)
        }).toMutableList()
        val adapter = PokemonRecyclerAdapter(activity, pokemons, myItemClickListener, true)
        recyclerView.adapter = adapter
        val teamButton: Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.setOnClickListener {
            loadTeamMenu(activity)
        }
    }

    private fun loadTeamMenu(activity: MainActivity) {
        activity.setContentView(R.layout.team_layout)
        val backButton: Button = activity.findViewById(R.id.teamMenuBackButton)
        backButton.setOnClickListener {
            if (activity.trainer!!.team.size > 0) {
                loadPokemonMenu(activity)
                SaveManager.save(activity)
            } else
                Toast.makeText(activity, "You need at least one Pokemon on your team.", Toast.LENGTH_SHORT).show()
        }
        activity.trainer!!.team.clear()
        val teamRecyclerView = activity.findViewById<RecyclerView>(R.id.teamRecyclerView)
        teamRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemonsRecyclerView = activity.findViewById<RecyclerView>(R.id.pokemonsRecyclerView)
        pokemonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team + activity.trainer!!.pokemons.filter {
            !activity.trainer!!.team.contains(it)
        }).toMutableList()
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (activity.trainer!!.team.size < 6 && !activity.trainer!!.team.contains(pokemons[position])) {
                activity.trainer!!.team.add(pokemons[position])
                teamRecyclerView.adapter =
                    TeamMemberRecyclerAdapter(activity, activity.trainer!!.team) {}
                activity.trainer!!.pokemons =
                    ArrayList(activity.trainer!!.team + activity.trainer!!.pokemons.filter { pokemon ->
                        !activity.trainer!!.team.contains(pokemon)
                    })
            }
        }
        pokemonsRecyclerView.adapter = TeamMemberRecyclerAdapter(activity, pokemons, myItemClickListener)
    }
}