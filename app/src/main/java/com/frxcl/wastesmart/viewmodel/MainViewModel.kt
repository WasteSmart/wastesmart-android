package com.frxcl.wastesmart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.data.remote.response.EncyclopediaOrganicResponse
import com.frxcl.wastesmart.data.remote.response.PredictResponse
import com.frxcl.wastesmart.data.remote.response.ResultItem
import com.frxcl.wastesmart.data.remote.response.TipsResponse
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: Repository): ViewModel() {
    private val _funFactsData = MutableLiveData<List<String?>?>()
    val funFactsData: MutableLiveData<List<String?>?> get() = _funFactsData

    private val _tipsData = MutableLiveData<TipsResponse?>()
    val tipsData: LiveData<TipsResponse?> get() = _tipsData

    fun getFunFacts() {
        repository.getFunFacts { result ->
            if (result != null) {
                _funFactsData.value = result.funfacts
            }
        }
    }

    fun getTips() {
        repository.getTips { result ->
            _tipsData.value = result
        }
    }
}