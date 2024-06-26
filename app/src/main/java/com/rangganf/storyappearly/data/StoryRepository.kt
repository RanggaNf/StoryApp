package com.rangganf.storyappearly.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rangganf.storyappearly.data.remote.network.ApiService
import com.rangganf.storyappearly.data.remote.response.login.LoginResponse
import com.rangganf.storyappearly.data.remote.response.Register.RegisterResponse
import com.rangganf.storyappearly.data.remote.response.stories.StoriesResponse
import com.rangganf.storyappearly.data.remote.response.stories.UploadStoryResponse
import com.rangganf.storyappearly.data.remote.response.stories.Story
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {

    // Mendapatkan daftar cerita menggunakan Paging
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
    fun getStoriesWithLocation(): LiveData<Result<StoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    // Mengirim cerita baru
    fun postStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<UploadStoryResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.postStory(file, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("StoryRepository", "postStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    // Registrasi pengguna baru
    fun postSignUp(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.postSignUp(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("StoryRepository", "postSignUp: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    // Proses login
    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("StoryRepository", "postLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}
