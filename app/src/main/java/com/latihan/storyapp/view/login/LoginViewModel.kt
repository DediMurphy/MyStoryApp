package com.latihan.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.storyapp.data.api.repository.StoryRepository
import com.latihan.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

   fun saveToken(userModel: UserModel) {
       viewModelScope.launch {
           repository.saveToken(userModel)
       }
   }

    fun login(email: String, password: String) = repository.login(email, password)
}