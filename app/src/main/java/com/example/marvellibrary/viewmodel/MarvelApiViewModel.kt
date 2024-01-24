package com.example.marvellibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvellibrary.model.api.MarvelApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelApiViewModel @Inject constructor(
    private val repo: MarvelApiRepo
) : ViewModel() {
    val result = repo.characters
    val queryText = MutableStateFlow("")
    private val queryInput = Channel<String>(Channel.CONFLATED)

    init {
        retrieveCharacters()
    }

    private fun retrieveCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            queryInput.receiveAsFlow()
                .filter { validateQuery(it) }
                .debounce(1000)
                .collect {
                    repo.query(it)
                }
        }
    }

    private fun validateQuery(query: String): Boolean = query.length >= 2
    fun onQueryUpdate(input: String) {
        queryText.value = input
        queryInput.trySend(input)
    }
}