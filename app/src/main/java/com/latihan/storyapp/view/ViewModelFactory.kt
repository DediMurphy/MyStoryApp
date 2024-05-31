package com.latihan.storyapp.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.latihan.storyapp.data.api.repository.StoryRepository
import com.latihan.storyapp.di.Injection
import com.latihan.storyapp.view.login.LoginViewModel
import com.latihan.storyapp.view.main.MainViewModel
import com.latihan.storyapp.view.maps.MapsViewModel
import com.latihan.storyapp.view.signup.SignupViewModel

class ViewModelFactory(private val repository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            SignupViewModel::class.java -> SignupViewModel(repository)
            LoginViewModel::class.java -> LoginViewModel(repository)
            MainViewModel::class.java -> MainViewModel(repository)
            MapsViewModel::class.java -> MapsViewModel(repository)
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } as T


    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}