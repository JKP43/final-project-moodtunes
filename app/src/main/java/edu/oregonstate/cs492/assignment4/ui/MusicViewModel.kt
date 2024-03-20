package edu.oregonstate.cs492.assignment4.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.assignment4.data.MusicForecast
import edu.oregonstate.cs492.assignment4.data.MusicRepository
import edu.oregonstate.cs492.assignment4.data.MusicService
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private val repository = MusicRepository(MusicService.create())

    private val _music = MutableLiveData<MusicForecast?>(null)

    val music: LiveData<MusicForecast?> = _music

    private val _error = MutableLiveData<Throwable?>(null)

    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)

    val loading: LiveData<Boolean> = _loading

    fun loadMusic(clientId: String, tags: String?, limit: Int?) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadMusic(clientId, tags, limit)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _music.value = result.getOrNull()
        }
    }
}