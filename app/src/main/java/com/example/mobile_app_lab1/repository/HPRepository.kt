package com.example.mobile_app_lab1.repository

import com.example.mobile_app_lab1.repository.model.Character
import retrofit2.Response

class HPRepository {
    suspend fun getCharactersListResponse(): Response<List<Character>> =
        HPService.hpService.getCharactersList()

    suspend fun getCharacterDetailsResponse(characterId: String): Response<List<Character>> =
        HPService.hpService.getCharacterDetails(characterId)

}