package com.example.app_1a.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_1a.data.api.external.ExternalGame
import com.example.app_1a.data.api.external.ExternalRetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FreeGamesViewModel : ViewModel() {

    private val _games = MutableStateFlow<List<ExternalGame>>(emptyList())
    val games: StateFlow<List<ExternalGame>> = _games

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            try {
                val gameList = ExternalRetrofitClient.api.getPopularGames()
                _games.value = gameList
            } catch (e: Exception) {
                e.printStackTrace()
                // Aquí podrías manejar el error
            }
        }
    }
}