package com.example.sikhoapp.presentation.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sikhoapp.data.api.ApiResult
import com.example.sikhoapp.domain.AnimeDetailUseCase
import com.example.sikhoapp.domain.AnimeListUseCase
import com.example.sikhoapp.model.AnimDetailsResponse
import com.example.sikhoapp.model.AnimeListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    animListUseCase: AnimeListUseCase,
    private val animDetailUseCase: AnimeDetailUseCase
) : ViewModel() {

    val animeListResponse = mutableStateOf<AnimeListResponse?>(null)
    val animeDetailResponse = mutableStateOf<AnimDetailsResponse?>(null)
    val loader = mutableStateOf<Boolean>(true)
    val isFullScreen = mutableStateOf<Boolean>(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            animListUseCase.invokeApi().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        loader.value = false
                        animeListResponse.value = result.data
                    }

                    is ApiResult.Error -> {
                        loader.value = false
                    }

                    is ApiResult.Loading -> {
                        loader.value = true
                    }
                }
            }

        }
    }

    fun callAnimeDetailApi(id: Int?) {
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            animDetailUseCase.invokeApi(id).collect { result ->
                loader.value = result is ApiResult.Loading
                when (result) {
                    is ApiResult.Success -> {
                        animeDetailResponse.value = result.data
                    }

                    is ApiResult.Error -> {}
                    is ApiResult.Loading -> {}
                }
            }
        }
    }

}