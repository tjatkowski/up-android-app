package com.example.mobile_app_lab1.repository

import com.example.mobile_app_lab1.repository.model.Character
import retrofit2.Response

class HPRepository {
    suspend fun getHPResponse(): Response<List<Character>> =
        HPService.hpService.getStarResponse()

}