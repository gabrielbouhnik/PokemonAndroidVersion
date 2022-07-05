package com.pokemon.android.version.repository

import com.pokemon.android.version.MainActivity

interface Repository<T> {
    fun loadData(activity: MainActivity): List<T>
}