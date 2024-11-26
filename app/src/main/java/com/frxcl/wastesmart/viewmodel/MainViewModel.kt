package com.frxcl.wastesmart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.frxcl.wastesmart.util.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences): ViewModel() {
    fun saveSetupState(isSetupDone: Boolean) {
        viewModelScope.launch {
            pref.saveSetupState(isSetupDone)
        }
    }

    fun getSetupState(): LiveData<Boolean> {
        return pref.getSetupState().asLiveData()
    }

    fun saveUserName(username: String) {
        viewModelScope.launch {
            pref.saveUserName(username)
        }
    }

    fun getUserName(): LiveData<String?> {
        return pref.getUserName().asLiveData()
    }
}