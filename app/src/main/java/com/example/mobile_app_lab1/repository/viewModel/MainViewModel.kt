package com.example.mobile_app_lab1.repository.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app_lab1.UiState
import com.example.mobile_app_lab1.repository.HPRepository
import com.example.mobile_app_lab1.repository.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val hpRepository = HPRepository()

    private val mutableHpData = MutableLiveData<UiState<List<Character>>>()
    val immutableHpData: LiveData<UiState<List<Character>>> = mutableHpData

    fun getData() {
        mutableHpData.postValue(UiState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = hpRepository.getCharactersListResponse()
                Log.d("MainViewModel", "request code ${request.code()}")
                if(request.isSuccessful) {
                    val characters = request.body()
                    mutableHpData.postValue(UiState(data = characters))
                } else {
                    mutableHpData.postValue(UiState(error = "Request error ${request.code()}"))

                }

            } catch (e: Exception) {
                mutableHpData.postValue(UiState(error = "Exception $e"))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }

    fun getCharacterDetails(characterId: String) {
        mutableHpData.postValue(UiState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = hpRepository.getCharacterDetailsResponse(characterId)
                Log.d("MainViewModel", "request code ${request.code()} for $characterId")
                if(request.isSuccessful) {
                    val characters = request.body()
                    mutableHpData.postValue(UiState(data = characters))
                } else {
                    mutableHpData.postValue(UiState(error = "Request error ${request.code()}"))

                }

            } catch (e: Exception) {
                mutableHpData.postValue(UiState(error = "Exception $e"))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }

    }
}