package com.rangganf.storyappearly.utils

import com.rangganf.storyappearly.data.remote.response.stories.StoriesResponse
import com.rangganf.storyappearly.data.remote.response.stories.Story
import com.rangganf.storyappearly.data.remote.response.stories.UploadStoryResponse

// Objek utilitas untuk menghasilkan data dummy
object DataDummy {

    // Fungsi untuk menghasilkan dummy StoriesResponse dengan 20 cerita palsu
    fun generateDummyStories(): StoriesResponse {
        val listStory = ArrayList<Story>()
        for (i in 1..20) {
            val story = Story(
                createdAt = "2022-02-22T22:22:22Z",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650"
            )
            listStory.add(story)
        }

        return StoriesResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    // Fungsi untuk menghasilkan dummy UploadStoryResponse
    fun generateDummyCreateStory(): UploadStoryResponse {
        return UploadStoryResponse(
            error = false,
            message = "success"
        )
    }
}
