package com.latihan.storyapp.di

import android.content.Context
import android.util.Log
import com.latihan.storyapp.data.api.repository.StoryRepository
import com.latihan.storyapp.data.api.retrofit.ApiConfig
import com.latihan.storyapp.data.pref.UserPreference
import com.latihan.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        Log.d("STATUS TOKEN",user.token)
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref,apiService)
    }
}