package com.rangganf.storyappearly.data.remote.network

import com.rangganf.storyappearly.data.remote.response.login.LoginResponse
import com.rangganf.storyappearly.data.remote.response.Register.RegisterResponse
import com.rangganf.storyappearly.data.remote.response.stories.UploadStoryResponse
import com.rangganf.storyappearly.data.remote.response.stories.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : StoriesResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int =1,
    ) : StoriesResponse
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadStoryResponse
}