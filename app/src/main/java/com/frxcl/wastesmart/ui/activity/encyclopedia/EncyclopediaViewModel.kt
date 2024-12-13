package com.frxcl.wastesmart.ui.activity.encyclopedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.data.remote.response.EncyclopediaNonOrganicResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaOrganicResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaToxicResponse
import com.frxcl.wastesmart.data.remote.response.ExamplesItem
import com.frxcl.wastesmart.data.remote.response.ExamplesItemNonOrganic
import com.frxcl.wastesmart.data.remote.response.ExamplesItemToxic

class EncyclopediaViewModel(private val repository: Repository): ViewModel() {
    private val _encData = MutableLiveData<EncyclopediaResponse?>()
    val encData: LiveData<EncyclopediaResponse?> get() = _encData

    private val _encOrganicData = MutableLiveData<EncyclopediaOrganicResponse?>()
    val encOrganicData: LiveData<EncyclopediaOrganicResponse?> get() = _encOrganicData

    private val _encOrganicExampleData = MutableLiveData<List<ExamplesItem>?>()
    val encOrganicExampleData: LiveData<List<ExamplesItem>?> get() = _encOrganicExampleData

    private val _encNonOrganicData = MutableLiveData<EncyclopediaNonOrganicResponse?>()
    val encNonOrganicData: LiveData<EncyclopediaNonOrganicResponse?> get() = _encNonOrganicData

    private val _encNonOrganicExampleData = MutableLiveData<List<ExamplesItemNonOrganic>?>()
    val encNonOrganicExampleData: LiveData<List<ExamplesItemNonOrganic>?> get() = _encNonOrganicExampleData

    private val _encToxicData = MutableLiveData<EncyclopediaToxicResponse?>()
    val encToxicData: LiveData<EncyclopediaToxicResponse?> get() = _encToxicData

    private val _encToxicExampleData = MutableLiveData<List<ExamplesItemToxic>?>()
    val encToxicExampleData: LiveData<List<ExamplesItemToxic>?> get() = _encToxicExampleData

    fun getEncyclopedia() {
        repository.getEncyclopedia { result ->
            _encData.postValue(result)
        }
    }

    fun getEncyclopediaOrganic() {
        repository.getEncyclopediaOrganic { result ->
            _encOrganicData.value = result
        }
    }

    fun getEncyclopediaOrganicExample() {
        repository.getEncyclopediaOrganic { result ->
            _encOrganicExampleData.value = result.typeOfWaste?.examples as List<ExamplesItem>?
        }
    }

    fun getEncyclopediaNonOrganic() {
        repository.getEncyclopediaNonOrganic { result ->
            _encNonOrganicData.value = result
        }
    }

    fun getEncyclopediaNonOrganicExample() {
        repository.getEncyclopediaNonOrganic { result ->
            _encNonOrganicExampleData.value = result.typeOfWaste?.examples as List<ExamplesItemNonOrganic>?
        }
    }

    fun getEncyclopediaToxicExample() {
        repository.getEncyclopediaToxic { result ->
            _encToxicExampleData.value = result.typeOfWaste?.examples as List<ExamplesItemToxic>?
        }
    }

    fun getEncyclopediaToxic() {
        repository.getEncyclopediaToxic { result ->
            _encToxicData.value = result
        }
    }
}