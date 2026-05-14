package com.rangganf.storyappearly.ui.auth.Register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.StoryRepository
import com.rangganf.storyappearly.data.remote.response.Register.RegisterResponse
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
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private lateinit var signUpViewModel: RegisterViewModel
    private val dummySignUpResponse = DataDummy.generateDummyRegister()

    @Before
    fun setUp() {
        signUpViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when signUp Should Not Null and return success`() {
        val expectedSignUpResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedSignUpResponse.value = Result.Success(dummySignUpResponse)
        val name = "name"
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(storyRepository.postSignUp(name, email, password)).thenReturn(expectedSignUpResponse)

        val actualResponse = signUpViewModel.signUp(name, email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postSignUp(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedSignUpResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedSignUpResponse.value = Result.Error("network error")
        val name = "name"
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(storyRepository.postSignUp(name, email, password)).thenReturn(expectedSignUpResponse)

        val actualResponse = signUpViewModel.signUp(name, email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postSignUp(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}