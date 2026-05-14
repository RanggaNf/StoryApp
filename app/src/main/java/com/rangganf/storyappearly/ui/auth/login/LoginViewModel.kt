package com.rangganf.storyappearly.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.remote.response.login.LoginResponse
import com.rangganf.storyappearly.data.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    // Fungsi untuk melakukan login
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return storyRepository.postLogin(email, password)
    }
}
