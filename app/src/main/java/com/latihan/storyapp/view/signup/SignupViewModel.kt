package com.latihan.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.latihan.storyapp.data.api.repository.StoryRepository

class SignupViewModel(private val repository: StoryRepository): ViewModel() {
    fun register(name: String,email: String,password:String) =
        repository.getRegister(name, email, password)
}