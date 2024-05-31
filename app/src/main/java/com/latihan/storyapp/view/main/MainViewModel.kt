package com.latihan.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.latihan.storyapp.data.api.repository.StoryRepository
import com.latihan.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository, ) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}