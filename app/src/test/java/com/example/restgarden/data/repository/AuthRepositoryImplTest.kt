package com.example.restgarden.data.repository

import com.example.restgarden.data.api.AuthService
import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.model.response.SignInResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {
  
  @Mock
  lateinit var authServiceMock: AuthService
  private lateinit var authRepository: AuthRepository
  private val signInDummy = SignIn("user", "user")
  private val signInResponseDummy = SignInResponse("abcdefghij1234567890", "1234567890")
  
  @Before
  fun registerMock() {
    MockitoAnnotations.openMocks(this@AuthRepositoryImplTest)
    authRepository = AuthRepositoryImpl(authServiceMock)
  }
  
  @Test
  fun signIn_onSuccess() = runBlockingTest {
    `when`(authRepository.signIn(signInDummy)).thenReturn(
      Response.success(
        200,
        signInResponseDummy
      )
    )
    val actual = authRepository.signIn(signInDummy)
    verify(authServiceMock, times(1)).signIn(signInDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(signInResponseDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun signIn_onError() = runBlockingTest {
    `when`(authRepository.signIn(signInDummy)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = authRepository.signIn(signInDummy)
    verify(authServiceMock, times(1)).signIn(signInDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
    assertThat(actual.code()).isEqualTo(500)
  }
}
