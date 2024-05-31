package com.latihan.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.latihan.storyapp.data.api.repository.UserRepository
import com.latihan.storyapp.di.StoryInjection

class StoryViewFactory(private val repo: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            StoryViewModel::class.java -> StoryViewModel(repo)
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } as T

    companion object {
        @Volatile
        private var INSTANCE: StoryViewFactory? = null
        @JvmStatic
        fun getInstance(context: Context): StoryViewFactory {
            if (INSTANCE == null) {
                synchronized(StoryViewFactory::class.java) {
                    INSTANCE = StoryViewFactory(StoryInjection.provideRepository(context))
                }
            }
            return INSTANCE as StoryViewFactory
        }
    }
}