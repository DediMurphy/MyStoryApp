package com.latihan.storyapp.di

import android.content.Context
import android.util.Log
import com.latihan.storyapp.data.api.repository.UserRepository
import com.latihan.storyapp.data.api.retrofit.ApiConfig
import com.latihan.storyapp.data.api.room.StoryDatabase
import com.latihan.storyapp.data.pref.UserPreference
import com.latihan.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object StoryInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        Log.d("STATUS TOKEN",user.token)
        val apiService = ApiConfig.getApiService(user.token)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(storyDatabase,apiService)
    }
}