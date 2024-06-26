package com.rangganf.storyappearly.ui.story.createstory

import androidx.lifecycle.ViewModel
import com.rangganf.storyappearly.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

// ViewModel yang bertanggung jawab untuk berinteraksi dengan Repository dan menyediakan data untuk UI
class CreateStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    // Fungsi untuk mengirim cerita ke server
    fun postStory(file: MultipartBody.Part, description: RequestBody) =
        storyRepository.postStory(file, description)
}
