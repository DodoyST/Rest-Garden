package com.example.restgarden.data.repository

import com.example.restgarden.data.api.GraveService
import com.example.restgarden.data.model.Grave
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
class GraveRepositoryImplTest {
  
  @Mock
  lateinit var graveServiceMock: GraveService
  private lateinit var graveRepository: GraveRepository
  private val graveListDummy = listOf(
    Grave("1", "Grave1", 10, 1000000.0, "grave1", "12345", "Private", "grave1", "grave1"),
    Grave("2", "Grave2", 10, 1000000.0, "grave2", "67890", "Private", "grave2", "grave2"),
  )
  private val graveDummy = graveListDummy[0]
  
  @Before
  fun registerMock() {
    MockitoAnnotations.openMocks(this@GraveRepositoryImplTest)
    graveRepository = GraveRepositoryImpl(graveServiceMock)
  }
  
  @Test
  fun getAllGraves_onSuccess() = runBlockingTest {
    `when`(graveRepository.getAllGraves()).thenReturn(Response.success(200, graveListDummy))
    val actual = graveRepository.getAllGraves()
    verify(graveServiceMock, times(1)).getAll()
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(graveListDummy)
    assertThat(actual.body()).hasSize(2)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun getAllGraves_onError() = runBlockingTest {
    `when`(graveRepository.getAllGraves()).thenReturn(
      Response.error(500, "Error".toResponseBody(null))
    )
    val actual = graveRepository.getAllGraves()
    verify(graveServiceMock, times(1)).getAll()
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
    assertThat(actual.code()).isEqualTo(500)
  }
  
  @Test
  fun getGraveById() = runBlockingTest {
    `when`(graveRepository.getGraveById(graveDummy.id)).thenReturn(
      Response.success(
        200,
        graveDummy
      )
    )
    val actual = graveRepository.getGraveById(graveDummy.id)
    verify(graveServiceMock, times(1)).getById(graveDummy.id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(graveDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun getGraveById_onError() = runBlockingTest {
    `when`(graveRepository.getGraveById(graveDummy.id)).thenReturn(
      Response.error(500, "Error".toResponseBody(null))
    )
    val actual = graveRepository.getGraveById(graveDummy.id)
    verify(graveServiceMock, times(1)).getById(graveDummy.id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
    assertThat(actual.code()).isEqualTo(500)
  }
}
