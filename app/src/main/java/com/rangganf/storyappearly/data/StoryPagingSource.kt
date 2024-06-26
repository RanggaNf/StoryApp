package com.rangganf.storyappearly.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rangganf.storyappearly.data.remote.network.ApiService
import com.rangganf.storyappearly.data.remote.response.stories.Story

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        // nilai prevKey dari halaman terdekat jika anchorPosition tidak null
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            // Mendapatkan nilai halaman
            val page = params.key ?: INITIAL_PAGE_INDEX
            // Memanggil API untuk mendapatkan data story
            val responseData = apiService.getStories(page, params.loadSize)

            // Membuat objek LoadResult.Page dengan data yang diterima dari API
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            // Mengembalikan LoadResult.Error jika terjadi kesalahan
            LoadResult.Error(exception)
        }
    }
}
