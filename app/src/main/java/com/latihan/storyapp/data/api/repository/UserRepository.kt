package com.latihan.storyapp.data.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.latihan.storyapp.data.api.Result
import com.latihan.storyapp.data.api.StoryPagingSource
import com.latihan.storyapp.data.api.entity.StoryEntity
import com.latihan.storyapp.data.api.response.FileUploadResponse
import com.latihan.storyapp.data.api.response.ListStoryItem
import com.latihan.storyapp.data.api.response.Story
import com.latihan.storyapp.data.api.response.StoryResponse
import com.latihan.storyapp.data.api.retrofit.ApiService
import com.latihan.storyapp.data.database.QuoteRemoteMediator
import com.latihan.storyapp.data.api.room.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = QuoteRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllQuote()
            }
        ).liveData
    }

    fun getDetailStory(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(id).story
            emit(Result.Success(response))
        } catch(e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(multipartBody: MultipartBody.Part, requestBody: RequestBody): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage(multipartBody,requestBody)
            emit(Result.Success(response))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(storyDatabase,apiService)
            }.also { instance = it }
    }
}