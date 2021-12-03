package com.example.restgarden.data.repository

import com.example.restgarden.data.api.ApiService
import com.example.restgarden.data.model.Grave
import retrofit2.Response
import javax.inject.Inject

class GraveRepositoryImpl @Inject constructor(private val apiService: ApiService) :
  GraveRepository {
  override suspend fun getAllGraves(): Response<List<Grave>> = apiService.getAllGrave()
}
