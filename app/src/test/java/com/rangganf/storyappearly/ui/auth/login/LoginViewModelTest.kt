package com.rangganf.storyappearly.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.StoryRepository
import com.rangganf.storyappearly.data.remote.response.login.LoginResponse
import com.rangganf.storyappearly.utils.DataDummy
import com.rangganf.storyappearly.utils.getOrAwaitValue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when login Should Not Null and return success`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(storyRepository.postLogin(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postLogin(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Error("network error")
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(storyRepository.postLogin(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postLogin(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}