package com.latihan.storyapp.data.api.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.latihan.storyapp.data.api.Result
import com.latihan.storyapp.data.api.retrofit.ApiService
import com.latihan.storyapp.data.api.response.ErrorResponse
import com.latihan.storyapp.data.api.response.LoginResponse
import com.latihan.storyapp.data.api.response.RegisterResponse
import com.latihan.storyapp.data.api.response.StoryResponse
import com.latihan.storyapp.data.pref.UserModel
import com.latihan.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun getRegister(name: String,email: String,password: String): LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("Repository", "register user: $errorMessage ")
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d("Repository", "register user: $errorMessage ")
                emit(Result.Error(errorMessage.toString()))
            }
    }

    fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
            try {
                val response = apiService.getStoriesWithLocation()
                emit(Result.Success(response))
        } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d("Repository", "register user: $errorMessage ")
                emit(Result.Error(errorMessage.toString()))
            }
    }

    suspend fun saveToken(token: UserModel) {
        userPreference.saveToken(token)
    }

    fun getUser(): Flow<UserModel> {
        return userPreference.getUser()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference,apiService)
            }.also { instance = it }

    }
}