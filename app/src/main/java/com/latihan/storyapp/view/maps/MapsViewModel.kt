package com.latihan.storyapp.view.maps

import androidx.lifecycle.ViewModel
import com.latihan.storyapp.data.api.repository.StoryRepository


class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
}