package com.rangganf.storyappearly.data.remote.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rangganf.storyappearly.data.remote.response.stories.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<Story>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAllStories()
}