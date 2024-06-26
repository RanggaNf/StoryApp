package com.rangganf.storyappearly.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.StoryRepository
import com.rangganf.storyappearly.data.remote.response.stories.StoriesResponse
import com.rangganf.storyappearly.ui.maps.MapViewModel
import com.rangganf.storyappearly.utils.DataDummy
import com.rangganf.storyappearly.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

// Penggunaan runner Mockito untuk pengujian
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest{

    // Rule untuk menjalankan semua task executor secara instan pada pengujian
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mock untuk StoryRepository
    @Mock private lateinit var storyRepository: StoryRepository

    // ViewModel yang akan diuji
    private lateinit var mapViewModel: MapViewModel

    // Data palsu yang akan digunakan dalam pengujian
    private val dummyStoriesResponse = DataDummy.generateDummyStories()

    // Persiapan sebelum setiap pengujian
    @Before
    fun setUp() {
        mapViewModel = MapViewModel(storyRepository)
    }

    // Pengujian ketika fungsi getStoriesWithLocation dijalankan dan mengembalikan hasil yang sukses
    @Test
    fun `when getStoriesWithLocation Should Not Null and return success`() {
        // Mengatur respons yang diharapkan dari repository ketika getStoriesWithLocation dipanggil
        val expectedStoryResponse = MutableLiveData<Result<StoriesResponse>>()
        expectedStoryResponse.value = Result.Success(dummyStoriesResponse)
        `when`(storyRepository.getStoriesWithLocation()).thenReturn(expectedStoryResponse)

        // Menjalankan fungsi yang diuji dan memeriksa hasilnya
        val actualStories = mapViewModel.getStoriesWithLocation().getOrAwaitValue()
        Mockito.verify(storyRepository).getStoriesWithLocation()
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Success)
        Assert.assertEquals(dummyStoriesResponse.listStory.size, (actualStories as Result.Success).data.listStory.size)
    }

    // Pengujian ketika terjadi kesalahan jaringan dan fungsi getStoriesWithLocation mengembalikan error
    @Test
    fun `when Network Error Should Return Error`() {
        // Mengatur respons yang diharapkan dari repository ketika getStoriesWithLocation dipanggil
        val expectedStoryResponse = MutableLiveData<Result<StoriesResponse>>()
        expectedStoryResponse.value = Result.Error("network error")
        `when`(storyRepository.getStoriesWithLocation()).thenReturn(expectedStoryResponse)

        // Menjalankan fungsi yang diuji dan memeriksa hasilnya
        val actualStories = mapViewModel.getStoriesWithLocation().getOrAwaitValue()
        Mockito.verify(storyRepository).getStoriesWithLocation()
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Error)
    }
}
