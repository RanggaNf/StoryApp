package com.rangganf.storyappearly.di

import android.content.Context
import com.rangganf.storyappearly.data.StoryRepository
import com.rangganf.storyappearly.data.remote.network.ApiConfig

object Injection {
    // Fungsi untuk menyediakan repository
    fun provideRepository(context: Context): StoryRepository {
        // Mendapatkan instance dari ApiService melalui ApiConfig
        val apiService = ApiConfig.getApiService(context)

        // Membuat dan mengembalikan instance StoryRepository dengan ApiService yang telah disediakan
        return StoryRepository(apiService)
    }
}
