package com.example.restgarden.data.repository

import com.example.restgarden.data.model.Grave
import retrofit2.Response

interface GraveRepository {
  suspend fun getAllGraves(): Response<List<Grave>>
  suspend fun getGraveById(id: String): Response<Grave>
}
