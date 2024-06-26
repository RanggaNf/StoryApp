package com.rangganf.storyappearly.data.remote.response.stories
import com.google.gson.annotations.SerializedName

data class UploadStoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)
