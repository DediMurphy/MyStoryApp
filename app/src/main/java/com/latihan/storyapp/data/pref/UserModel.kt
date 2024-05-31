package com.latihan.storyapp.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)