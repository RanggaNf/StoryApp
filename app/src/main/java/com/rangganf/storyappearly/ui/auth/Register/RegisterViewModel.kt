package com.rangganf.storyappearly.ui.auth.Register

import androidx.lifecycle.ViewModel
import com.rangganf.storyappearly.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    // Fungsi untuk melakukan pendaftaran (Register)
    fun signUp(name: String, email: String, password: String) = storyRepository.postSignUp(name, email, password)
}
