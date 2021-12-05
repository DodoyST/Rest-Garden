package com.example.restgarden.data.repository

import com.example.restgarden.data.api.GraveService
import com.example.restgarden.data.model.Grave
import retrofit2.Response
import javax.inject.Inject

class GraveRepositoryImpl @Inject constructor(private val graveService: GraveService) :
  GraveRepository {
  override suspend fun getAllGraves(): Response<List<Grave>> = graveService.getAll()
  override suspend fun getGraveById(id: String): Response<Grave> = graveService.getById(id)
}
