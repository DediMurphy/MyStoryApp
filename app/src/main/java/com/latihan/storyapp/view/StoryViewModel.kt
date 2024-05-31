package com.latihan.storyapp.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.latihan.storyapp.data.api.entity.StoryEntity
import com.latihan.storyapp.data.api.repository.UserRepository
import com.latihan.storyapp.data.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoryViewModel(private val repo: UserRepository) : ViewModel() {

    val getStory: LiveData<PagingData<StoryEntity>> =
        repo.getStories().cachedIn(viewModelScope)

    fun getDetailStory(id: String) = repo.getDetailStory(id)

    fun uploadStory(multipartBody: MultipartBody.Part, requestBody: RequestBody) =
        repo.uploadStory(multipartBody, requestBody)
}