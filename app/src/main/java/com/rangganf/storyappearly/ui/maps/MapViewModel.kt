package com.rangganf.storyappearly.ui.maps

import androidx.lifecycle.ViewModel
import com.rangganf.storyappearly.data.StoryRepository

/**
 * ViewModel untuk menangani logika terkait tampilan peta (maps) dan data cerita.
 *
 * @param storyRepository Repository untuk mengakses data cerita.
 */
class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    /**
     * Mengambil daftar cerita dengan informasi lokasi dari repository.
     *
     * @return LiveData<Result<List<Story>>> yang berisi hasil operasi.
     */
    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}
