package com.rangganf.storyappearly.ui.story.liststory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.rangganf.storyappearly.data.StoryRepository
import com.rangganf.storyappearly.data.remote.response.stories.Story
import com.rangganf.storyappearly.utils.DataDummy
import com.rangganf.storyappearly.utils.MainDispatcherRule
import com.rangganf.storyappearly.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {

    // Rule untuk menjalankan kode secara synchronous pada pengujian
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Rule untuk menjalankan kode pada dispatcher utama selama pengujian
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    // Mock repository untuk menggantikan implementasi sebenarnya
    @Mock
    private lateinit var storyRepository: StoryRepository

    // Pengujian ketika data berhasil dimuat
    @Test
    fun `when getStories Should Not Null and Return Data`() = runTest {
        // Membuat data dummy StoriesResponse
        val dummyStoriesResponse = DataDummy.generateDummyStories()

        // Menginisialisasi data PagingData dari PagingSource
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStoriesResponse.listStory)

        // Menggunakan MutableLiveData untuk mensimulasikan respons dari repository
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data

        // Menentukan respons yang diharapkan dari repository menggunakan Mockito
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStories)

        // Membuat instance ListStoryViewModel dengan repository palsu
        val listStoryViewModel = ListStoryViewModel(storyRepository)

        // Mengambil data aktual dari viewModel
        val actualStories: PagingData<Story> = listStoryViewModel.stories.getOrAwaitValue()

        // Menggunakan AsyncPagingDataDiffer untuk membandingkan hasil aktual dengan hasil yang diharapkan
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        // Menambahkan asert untuk memeriksa kondisi yang diminta
        // a. Memastikan data tidak null
        Assert.assertNotNull(differ.snapshot())
        // b. Memastikan jumlah data sesuai dengan yang diharapkan
        Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)
        // c. Memastikan data pertama yang dikembalikan sesuai
        Assert.assertEquals(dummyStoriesResponse.listStory[0], differ.snapshot()[0])
    }

    // Pengujian ketika tidak ada data yang dikembalikan
    @Test
    fun `when getStoriesEmpty Should Return Empty Data`() = runTest {
        // Membuat data PagingData kosong
        val data: PagingData<Story> = PagingData.from(emptyList())

        // Menggunakan MutableLiveData untuk mensimulasikan respons dari repository
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data

        // Menentukan respons yang diharapkan dari repository menggunakan Mockito
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStories)

        // Membuat instance ListStoryViewModel dengan repository palsu
        val listStoryViewModel = ListStoryViewModel(storyRepository)

        // Mengambil data aktual dari viewModel
        val actualStories: PagingData<Story> = listStoryViewModel.stories.getOrAwaitValue()

        // Menggunakan AsyncPagingDataDiffer untuk membandingkan hasil aktual dengan hasil yang diharapkan
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        // Menambahkan asert untuk memeriksa kondisi yang diminta
        // a. Memastikan data tidak null
        Assert.assertNotNull(differ.snapshot())
        // b. Memastikan jumlah data yang dikembalikan nol
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

// Class yang mengimplementasikan PagingSource untuk pengujian
class StoryPagingSource : PagingSource<Int, Story>() {
    companion object {
        // Fungsi untuk membuat snapshot PagingData dari daftar item
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    // Fungsi untuk mendapatkan kunci refresh
    override fun getRefreshKey(state: PagingState<Int, Story>): Int {
        return 0
    }

    // Fungsi untuk memuat data dengan parameter LoadParams
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        // Mengembalikan halaman data kosong
        return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
    }
}

// Callback yang tidak melakukan apa-apa untuk memperbarui daftar
val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
