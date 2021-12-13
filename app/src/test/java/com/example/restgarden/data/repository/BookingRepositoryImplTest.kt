package com.example.restgarden.data.repository

import com.example.restgarden.data.api.TransactionService
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.data.model.request.ExtendAssignRequest
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
class BookingRepositoryImplTest {
  
  @Mock
  lateinit var transactionServiceMock: TransactionService
  private lateinit var transactionRepository: TransactionRepository
  private val transactionListDummy = listOf(
    Booking(
      "1",
      "grave1",
      "grave1",
      1000000.00,
      "reserved",
      1000000.00,
      123456,
      "transaction1",
      "1",
      "1",
      1,
      "grave1"
    ),
    Booking(
      "2",
      "grave2",
      "grave2",
      1000000.00,
      "reserved",
      1000000.00,
      123456,
      "transaction2",
      "2",
      "1",
      1,
      "grave2"
    )
  )
  private val transactionDummy = transactionListDummy[0]
  private val transactionRequestDummy = BookingTransactionRequest("transaction1", "1", "1", 1)
  private val reSubscribeAssignRequestDummy = ExtendAssignRequest("1")
  
  @Before
  fun registerMock() {
    MockitoAnnotations.openMocks(this@BookingRepositoryImplTest)
    transactionRepository = TransactionRepositoryImpl(transactionServiceMock)
  }
  
  @Test
  fun getAllBooking_onSuccess() = runBlockingTest {
    `when`(transactionRepository.getAllBooking(transactionListDummy[0].id)).thenReturn(
      Response.success(
        200,
        transactionListDummy
      )
    )
    val actual = transactionRepository.getAllBooking(transactionListDummy[0].id)
    verify(transactionServiceMock, times(1))
      .getAllBooking(transactionListDummy[0].id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(transactionListDummy)
    assertThat(actual.body()).hasSize(2)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun getAllBooking_onError() = runBlockingTest {
    `when`(transactionRepository.getAllBooking(transactionListDummy[0].id)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = transactionRepository.getAllBooking(transactionListDummy[0].id)
    verify(transactionServiceMock, times(1)).getAllBooking(transactionListDummy[0].id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
  }
  
  @Test
  fun getBookingById_onSuccess() = runBlockingTest {
    `when`(transactionRepository.getBookingById(transactionDummy.id)).thenReturn(
      Response.success(
        200,
        transactionDummy
      )
    )
    val actual = transactionRepository.getBookingById(transactionDummy.id)
    verify(transactionServiceMock, times(1))
      .getBookingById(transactionDummy.id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(transactionDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun getBookingById_onError() = runBlockingTest {
    `when`(transactionRepository.getBookingById(transactionDummy.id)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = transactionRepository.getBookingById(transactionDummy.id)
    verify(transactionServiceMock, times(1)).getBookingById(transactionDummy.id)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
  }
  
  @Test
  fun booking_onSuccess() = runBlockingTest {
    `when`(transactionRepository.booking(transactionRequestDummy)).thenReturn(
      Response.success(
        200,
        transactionDummy
      )
    )
    val actual = transactionRepository.booking(transactionRequestDummy)
    verify(transactionServiceMock, times(1))
      .booking(transactionRequestDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(transactionDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun booking_onError() = runBlockingTest {
    `when`(transactionRepository.booking(transactionRequestDummy)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = transactionRepository.booking(transactionRequestDummy)
    verify(transactionServiceMock, times(1)).booking(transactionRequestDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
  }
  
  @Test
  fun buy_onSuccess() = runBlockingTest {
    `when`(transactionRepository.buy(transactionRequestDummy)).thenReturn(
      Response.success(
        200,
        transactionDummy
      )
    )
    val actual = transactionRepository.buy(transactionRequestDummy)
    verify(transactionServiceMock, times(1))
      .buy(transactionRequestDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNotNull()
    assertThat(actual.body()).isEqualTo(transactionDummy)
    assertThat(actual.code()).isEqualTo(200)
  }
  
  @Test
  fun buy_onError() = runBlockingTest {
    `when`(transactionRepository.buy(transactionRequestDummy)).thenReturn(
      Response.error(
        500,
        "Error".toResponseBody(null)
      )
    )
    val actual = transactionRepository.buy(transactionRequestDummy)
    verify(transactionServiceMock, times(1)).buy(transactionRequestDummy)
    assertThat(actual).isNotNull()
    assertThat(actual.body()).isNull()
    assertThat(actual.errorBody()).isNotNull()
  }
  
  @Test
  fun cancelBooking() = runBlockingTest {
    val actual = transactionRepository.cancelBooking(transactionDummy.id)
    verify(transactionServiceMock, times(1)).cancelBooking(transactionDummy.id)
  }
  
  @Test
  fun reSubscribe() = runBlockingTest {
    val actual =
      transactionRepository.reSubscribeBooking(reSubscribeAssignRequestDummy.reservationId)
    verify(transactionServiceMock, times(1)).reSubscribe(reSubscribeAssignRequestDummy)
  }
  
  @Test
  fun assign() = runBlockingTest {
    val actual = transactionRepository.assignBooking(reSubscribeAssignRequestDummy.reservationId)
    verify(transactionServiceMock, times(1)).assign(reSubscribeAssignRequestDummy)
  }
}
