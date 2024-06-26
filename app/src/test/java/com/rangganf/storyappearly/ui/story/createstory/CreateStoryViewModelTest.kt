package com.rangganf.storyappearly.ui.story.createstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.StoryRepository
import com.rangganf.storyappearly.data.remote.response.stories.UploadStoryResponse
import com.rangganf.storyappearly.utils.DataDummy
import com.rangganf.storyappearly.utils.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewModelTest {

    // Rule untuk menjalankan semua task executor secara instan pada pengujian
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mock untuk StoryRepository
    @Mock private lateinit var storyRepository: StoryRepository

    // ViewModel yang akan diuji
    private lateinit var createStoryViewModel: CreateStoryViewModel

    // Data palsu yang akan digunakan dalam pengujian
    private val dummyResponse = DataDummy.generateDummyCreateStory()

    // Fungsi untuk melakukan persiapan sebelum setiap pengujian
    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewModel(storyRepository)
    }

    // Pengujian ketika fungsi postStory dijalankan dan mengembalikan hasil yang sukses
    @Test
    fun `when postStory Should Not Null and return success`() {
        val descriptionText = "Description text"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo name",
            "photo.jpg",
            requestImageFile
        )

        // Mengatur respons yang diharapkan dari repository ketika postStory dipanggil
        val expectedPostResponse = MutableLiveData<Result<UploadStoryResponse>>()
        expectedPostResponse.value = Result.Success(dummyResponse)
        Mockito.`when`(storyRepository.postStory(imageMultipart, description)).thenReturn(expectedPostResponse)

        // Menjalankan fungsi yang diuji dan memeriksa hasilnya
        val actualResponse = createStoryViewModel.postStory(imageMultipart, description).getOrAwaitValue()
        Mockito.verify(storyRepository).postStory(imageMultipart, description)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyResponse.error, (actualResponse as Result.Success).data.error)
    }

    // Pengujian ketika terjadi kesalahan jaringan dan fungsi postStory mengembalikan error
    @Test
    fun `when Network Error Should Return Error`() {
        val descriptionText = "Description text"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo name",
            "photo.jpg",
            requestImageFile
        )

        // Mengatur respons yang diharapkan dari repository ketika postStory dipanggil
        val expectedPostResponse = MutableLiveData<Result<UploadStoryResponse>>()
        expectedPostResponse.value = Result.Error("network error")
        Mockito.`when`(storyRepository.postStory(imageMultipart, description)).thenReturn(expectedPostResponse)

        // Menjalankan fungsi yang diuji dan memeriksa hasilnya
        val actualResponse = createStoryViewModel.postStory(imageMultipart, description).getOrAwaitValue()
        Mockito.verify(storyRepository).postStory(imageMultipart, description)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}
