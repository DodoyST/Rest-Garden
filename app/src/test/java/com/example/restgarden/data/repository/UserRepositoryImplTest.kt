package com.example.restgarden.data.repository

import com.example.restgarden.data.api.UserService
import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserRequest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
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
class UserRepositoryImplTest {
  
  @Mock
  lateinit var userServiceMock: UserService
  private lateinit var userRepository: UserRepository
  
  private val userDummy = User("1", "user", "user", "user", "user@user.com", "12345", "jl. user")
  private val userRequestDummy =
    UserRequest("name", "user", "user", "user@user.com", "12345", "jl. user")
  
  @Before
  fun registerMock() {
    MockitoAnnotations.openMocks(this@UserRepositoryImplTest)
    userRepository = UserRepositoryImpl(userServiceMock)
  }
  
  @Test
  fun getUserById_onSuccess() = runBlockingTest {
    `when`(userRepository.getUserById(userDummy.id)).thenReturn(Response.success(200, userDummy))
    val actual = userRepository.getUserById(userDummy.id)
    verify(userServiceMock, times(1)).getById(userDummy.id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(userDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun getUserById_onError() = runBlockingTest {
    `when`(userRepository.getUserById(userDummy.id)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = userRepository.getUserById(userDummy.id)
    verify(userServiceMock, times(1)).getById(userDummy.id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
    assertThat(actual.code()).isEqualTo(500)
  }
  
  @Test
  fun registerUser_onSuccess() = runBlockingTest {
    `when`(userRepository.registerUser(userRequestDummy)).thenReturn(
      Response.success(
        200,
        userDummy
      )
    )
    val actual = userRepository.registerUser(userRequestDummy)
    verify(userServiceMock, times(1)).register(userRequestDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(userDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun registerUser_onError() = runBlockingTest {
    `when`(userRepository.registerUser(userRequestDummy)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = userRepository.registerUser(userRequestDummy)
    verify(userServiceMock, times(1)).register(userRequestDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
    assertThat(actual.code()).isEqualTo(500)
  }
}
